package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.beans.general.SslSettings;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.enums.KeyStoreType;
import com.verisec.frejaeid.client.enums.TransactionContext;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.http.HttpServiceApi;
import com.verisec.frejaeid.client.service.AuthenticationService;
import com.verisec.frejaeid.client.service.OrganisationIdService;
import com.verisec.frejaeid.client.service.RequestValidationService;
import com.verisec.frejaeid.client.service.SignService;
import com.verisec.frejaeid.client.service.CustomIdentifierService;
import com.verisec.frejaeid.client.service.CustodianshipService;
import com.verisec.frejaeid.client.util.JsonService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    protected CustodianshipService custodianshipService;

    protected BasicClient(String serverCustomUrl, int pollingTimeoutInMillseconds,
                          TransactionContext transactionContext, HttpServiceApi httpService, String resourceServiceUrl)
            throws FrejaEidClientInternalException {
        jsonService = new JsonService();
        signService = new SignService(serverCustomUrl, pollingTimeoutInMillseconds, transactionContext, httpService,
                                      resourceServiceUrl);
        organisationIdService = new OrganisationIdService(serverCustomUrl, pollingTimeoutInMillseconds, httpService,
                                                          resourceServiceUrl);
        customIdentifierService = new CustomIdentifierService(serverCustomUrl, httpService, resourceServiceUrl);
        authenticationService = new AuthenticationService(serverCustomUrl, httpService, pollingTimeoutInMillseconds,
                                                          transactionContext, resourceServiceUrl);
        requestValidationService = new RequestValidationService();
        custodianshipService = new CustodianshipService(serverCustomUrl, httpService, resourceServiceUrl);
    }

    public abstract static class GenericBuilder {

        public static final Logger LOG = LogManager.getLogger(GenericBuilder.class);

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

        public GenericBuilder(SslSettings sslSettings,
                              FrejaEnvironment frejaEnvironment) throws FrejaEidClientInternalException {

            if (sslSettings.getSslContext() != null) {
                this.sslContext = sslSettings.getSslContext();
            } else {
                KeyStore keyStore = loadKeystore(sslSettings.getKeystorePath(), sslSettings.getKeystorePass());
                KeyStore trustStore = createTrustStore(keyStore, sslSettings);
                createSSLContext(keyStore, sslSettings.getKeystorePass(), trustStore);
            }

            setServerUrl(frejaEnvironment);
        }

        private KeyStore createTrustStore(KeyStore keyStore, SslSettings sslSettings) throws FrejaEidClientInternalException {
            KeyStore trustStore;
            if (sslSettings.getServerCertificatePath() != null) {
                trustStore = createTrustStoreWithCertificate(sslSettings.getServerCertificatePath());
            } else if (StringUtils.isNoneBlank(sslSettings.getTruststorePath(), sslSettings.getTruststorePass())) {
                trustStore = loadKeystore(sslSettings.getTruststorePath(), sslSettings.getTruststorePass());
            } else {
                trustStore = keyStore;
            }
            return trustStore;
        }

        private KeyStore loadKeystore(String path, String password) throws FrejaEidClientInternalException {
            KeyStore keyStore = null;

            int numberOfFails = 0;
            Exception lastException = null;

            for (KeyStoreType keyStoreType : KeyStoreType.values()) {
                try (InputStream keyStoreStream = new FileInputStream(path)) {
                    keyStore = KeyStore.getInstance(keyStoreType.getType());
                    keyStore.load(keyStoreStream, password.toCharArray());
                    LOG.debug("Loaded keystore from path path {}.", path);
                } catch (Exception ex) {
                    lastException = ex;
                    LOG.error("Failed to initialize SSL context with keystore type {} and path {}.", keyStoreType,
                              path, ex);
                    numberOfFails++;
                    continue;
                }
                break;
            }
            if (numberOfFails == KeyStoreType.values().length) {
                throw new FrejaEidClientInternalException(
                        String.format("Failed to load keystore at path %s with supported keystore types %s.",
                                      path, KeyStoreType.getAllKeyStoreTypes()), lastException);
            }

            return keyStore;

        }

        private void setServerUrl(FrejaEnvironment frejaEnvironment) {
            LOG.debug("Setting Freja environment {}", frejaEnvironment == FrejaEnvironment.TEST ?
                    FREJA_ENVIRONMENT_TEST : FREJA_ENVIRONMENT_PROD);
            serverCustomUrl = FrejaEnvironment.PRODUCTION.getServiceUrl();
            resourceServiceUrl = FrejaEnvironment.PRODUCTION.getResourceServiceUrl();
            if (frejaEnvironment == FrejaEnvironment.TEST) {
                serverCustomUrl = FrejaEnvironment.TEST.getServiceUrl();
                resourceServiceUrl = FrejaEnvironment.TEST.getResourceServiceUrl();
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
                throw new FrejaEidClientInternalException(
                        String.format("Failed to create trust store with certificate at path %s.", certificatePath),
                        ex);
            }
        }

        private void createSSLContext(KeyStore keyStore, String keystorePass, KeyStore trustStore)
                throws FrejaEidClientInternalException {
            try {
                KeyManagerFactory keyManagerFactory =
                        KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(keyStore, keystorePass.toCharArray());

                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                tmf.init(trustStore);

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

        public GenericBuilder setTestModeServerCustomUrl(String serverCustomUrl) {
            this.serverCustomUrl = serverCustomUrl;
            return this;
        }

        public GenericBuilder setTestModeResourceServiceCustomUrl(String resourceServiceCustomUrl) {
            this.resourceServiceUrl = resourceServiceCustomUrl;
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
