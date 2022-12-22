package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.enums.FrejaResourceEnvironment;
import com.verisec.frejaeid.client.enums.KeyStoreType;
import com.verisec.frejaeid.client.enums.TransactionContext;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.http.HttpServiceApi;
import com.verisec.frejaeid.client.service.AuthenticationService;
import com.verisec.frejaeid.client.service.OrganisationIdService;
import com.verisec.frejaeid.client.service.RequestValidationService;
import com.verisec.frejaeid.client.service.SignService;
import com.verisec.frejaeid.client.service.CustomIdentifierService;
import com.verisec.frejaeid.client.util.JsonService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicClient {

    private static final int DEFAULT_CONNECTION_TIMEOUT_IN_MILLISECONDS = 20000;
    private static final int DEFAULT_READ_TIMEOUT_IN_MILLISECONDS = 20000;
    protected static final int DEFAULT_POLLING_TIMEOUT_IN_MILLISECONDS = 3000;
    private static final int MINIMUM_POLLING_TIMEOUT_IN_MILLISECONDS = 1000;
    private static final int MAXIMUM_POLLING_TIMEOUT_IN_MILLISECONDS = 60000;
    protected JsonService jsonService;
    protected AuthenticationService authenticationService;
    protected SignService signService;
    protected OrganisationIdService organisationIdService;
    protected CustomIdentifierService customIdentifierService;
    protected RequestValidationService requestValidationService;

    protected BasicClient(String serverCustomUrl, int pollingTimeoutInMillseconds,
                          TransactionContext transactionContext, HttpServiceApi httpService, String resourceServerUrl)
            throws FrejaEidClientInternalException {
        jsonService = new JsonService();
        signService = new SignService(serverCustomUrl, pollingTimeoutInMillseconds, transactionContext, httpService);
        organisationIdService = new OrganisationIdService(serverCustomUrl, pollingTimeoutInMillseconds, httpService);
        customIdentifierService = new CustomIdentifierService(serverCustomUrl, httpService);
        authenticationService = new AuthenticationService(serverCustomUrl, httpService, pollingTimeoutInMillseconds,
                                                          transactionContext, resourceServerUrl);
        requestValidationService = new RequestValidationService();
    }

    public abstract static class GenericBuilder {

        public static final Logger LOG = LoggerFactory.getLogger(GenericBuilder.class);

        protected String serverCustomUrl = null;
        protected int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT_IN_MILLISECONDS;
        protected int readTimeout = DEFAULT_READ_TIMEOUT_IN_MILLISECONDS;
        protected int pollingTimeout = 0;
        protected static final String FREJA_ENVIRONMENT_PROD = "PRODUCTION";
        protected static final String FREJA_ENVIRONMENT_TEST = "TEST";
        protected HttpServiceApi httpService;
        protected SSLContext sslContext;
        protected TransactionContext transactionContext;
        protected String resourceServiceUrl = null;

        public GenericBuilder(SSLContext sslContext, FrejaEnvironment frejaEnvironment) {
            if (sslContext != null) {
                this.sslContext = sslContext;
            }
            setServerCustomUrl(frejaEnvironment);
        }

        public GenericBuilder(String keystorePath, String keystorePass, String certificatePath,
                              FrejaEnvironment frejaEnvironment)
                throws FrejaEidClientInternalException {
            int numberOfFails = 0;
            for (KeyStoreType keyStoreType : KeyStoreType.values()) {
                try (InputStream keyStoreStream = new FileInputStream(keystorePath)) {
                    KeyStore keyStore = KeyStore.getInstance(keyStoreType.getType());
                    keyStore.load(keyStoreStream, keystorePass.toCharArray());
                    LOG.debug("Creating SSL context with keystore file on path {}.", keystorePath);
                    createSSLContext(keyStore, keystorePass, certificatePath);

                } catch (FrejaEidClientInternalException | IOException | KeyStoreException
                        | NoSuchAlgorithmException | CertificateException e) {
                    LOG.error("Failed to initialize SSL context with keystore type {} and path {}.", keyStoreType,
                              keystorePath, e);
                    numberOfFails++;
                    continue;
                }
                break;
            }
            if (numberOfFails == KeyStoreType.values().length) {
                throw new FrejaEidClientInternalException(
                        String.format("Failed to initiate SSL context with supported keystore types %s.",
                                      KeyStoreType.getAllKeyStoreTypes()));
            }
            setServerCustomUrl(frejaEnvironment);
        }

        private void setServerCustomUrl(FrejaEnvironment frejaEnvironment) {
            LOG.debug("Setting Freja environment {}", frejaEnvironment == FrejaEnvironment.TEST ?
                    FREJA_ENVIRONMENT_TEST : FREJA_ENVIRONMENT_PROD);
            serverCustomUrl = FrejaEnvironment.PRODUCTION.getUrl();
            resourceServiceUrl = FrejaResourceEnvironment.PRODUCTION.getUrl();
            if (frejaEnvironment == FrejaEnvironment.TEST) {
                serverCustomUrl = FrejaEnvironment.TEST.getUrl();
                resourceServiceUrl = FrejaResourceEnvironment.TEST.getUrl();
            }
        }

        private KeyStore createTrustStoreWithCertificate(String certificatePath)
                throws FrejaEidClientInternalException {
            LOG.debug("Creating trust store with certificate on path {}.", certificatePath);
            try (InputStream stream = new FileInputStream(certificatePath)) {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                X509Certificate caCert = (X509Certificate) cf.generateCertificate(stream);
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null);
                trustStore.setCertificateEntry("rootCA", caCert);
                return trustStore;
            } catch (KeyStoreException | NoSuchAlgorithmException | IOException | CertificateException ex) {
                throw new FrejaEidClientInternalException("Failed to create trust store with certificate. ", ex);
            }
        }

        private void createSSLContext(KeyStore keyStore, String keystorePass, String certificatePath)
                throws FrejaEidClientInternalException {
            try {
                KeyManagerFactory keyManagerFactory =
                        KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(keyStore, keystorePass.toCharArray());
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                if (certificatePath != null) {
                    keyStore = createTrustStoreWithCertificate(certificatePath);
                }
                tmf.init(keyStore);
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(keyManagerFactory.getKeyManagers(), tmf.getTrustManagers(), null);
                LOG.debug("Successfully created SSL context.");
                this.sslContext = sslContext;
            } catch (KeyManagementException | KeyStoreException
                    | NoSuchAlgorithmException | UnrecoverableKeyException ex) {
                throw new FrejaEidClientInternalException("Failed to create SSL context. ", ex);
            }
        }

        /**
         * Connection timeout is time to establish the connection with remote
         * host.
         *
         * @param connectionTimeout in milliseconds on client side. Default
         *                          value is {@value #DEFAULT_CONNECTION_TIMEOUT_IN_MILLISECONDS}
         *                          milliseconds.
         * @return clientBuilder
         */
        public GenericBuilder setConnectionTimeout(int connectionTimeout) {
            LOG.debug("Connection timeout set to {}ms.", connectionTimeout);
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        /**
         * Read timeout is time waiting for data after the connection was
         * established (maximum time of inactivity between two data packages).
         *
         * @param readTimeout in milliseconds on client side. Default value is
         *                    {@value #DEFAULT_READ_TIMEOUT_IN_MILLISECONDS} milliseconds.
         * @return clientBuilder
         */
        public GenericBuilder setReadTimeout(int readTimeout) {
            LOG.debug("Read timeout set to {}ms.", readTimeout);
            this.readTimeout = readTimeout;
            return this;
        }

        /**
         * Polling timeout is time between two polls for final results.
         *
         * @param pollingTimeout in milliseconds on client side. Default value is
         *                       {@value #DEFAULT_POLLING_TIMEOUT_IN_MILLISECONDS} milliseconds.
         * @return clientBuilder
         */
        public GenericBuilder setPollingTimeout(int pollingTimeout) {
            LOG.debug("Polling timeout set to {}ms.", pollingTimeout);
            this.pollingTimeout = pollingTimeout;
            return this;
        }

        GenericBuilder setHttpService(HttpServiceApi httpService) {
            this.httpService = httpService;
            return this;
        }

        public GenericBuilder setTestModeCustomUrl(String serverCustomUrl) {
            this.serverCustomUrl = serverCustomUrl;
            return this;
        }

        /**
         * Only used for authentication and sign clients. Otherwise, method has
         * no effect and default values are set for:
         * <br> - custom identifier client to personal
         * <br> - organisation id client to organisational.
         *
         * @param transactionContext is context in which transaction is started.
         * @return clientBuilder
         */
        public GenericBuilder setTransactionContext(TransactionContext transactionContext) {
            LOG.debug("Transaction context set to {}.", transactionContext.getContext());
            this.transactionContext = transactionContext;
            return this;
        }

        public abstract <T extends BasicClient> T build() throws FrejaEidClientInternalException;

        protected void checkSetParameters() throws FrejaEidClientInternalException {
            if (pollingTimeout < MINIMUM_POLLING_TIMEOUT_IN_MILLISECONDS
                    || pollingTimeout > MAXIMUM_POLLING_TIMEOUT_IN_MILLISECONDS) {
                throw new FrejaEidClientInternalException(
                        String.format("Polling timeout must be between %s and %s seconds.",
                                      TimeUnit.MILLISECONDS.toSeconds(MINIMUM_POLLING_TIMEOUT_IN_MILLISECONDS),
                                      TimeUnit.MILLISECONDS.toSeconds(MAXIMUM_POLLING_TIMEOUT_IN_MILLISECONDS)));
            }
            if (transactionContext == null) {
                transactionContext = TransactionContext.PERSONAL;
            }
        }

    }

}
