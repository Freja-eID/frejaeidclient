package com.verisec.frejaeid.client.client.impl;

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
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class BasicClient {

    private static final int DEFAULT_CONNECTION_TIMEOUT_IN_MILLSECONDS = 20000;
    private static final int DEFAULT_READ_TIMEOUT_IN_MILLSECONDS = 20000;
    private static final int DEFAULT_POLLING_TIMEOUT_IN_MILLSECONDS = 3000;
    protected JsonService jsonService;
    protected AuthenticationService authenticationService;
    protected SignService signService;
    protected OrganisationIdService organisationIdService;
    protected CustomIdentifierService customIdentifierService;
    protected RequestValidationService requestValidationService;

    protected BasicClient(String serverCustomUrl, int pollingTimeoutInMillseconds, TransactionContext transactionContext, HttpServiceApi httpService) throws FrejaEidClientInternalException {
        jsonService = new JsonService();
        String serverAddress = serverCustomUrl;
        signService = new SignService(serverAddress, pollingTimeoutInMillseconds, transactionContext, httpService);
        organisationIdService = new OrganisationIdService(serverAddress, pollingTimeoutInMillseconds, httpService);
        customIdentifierService = new CustomIdentifierService(serverAddress, httpService);
        authenticationService = new AuthenticationService(serverAddress, httpService, pollingTimeoutInMillseconds, transactionContext);
        requestValidationService = new RequestValidationService();
    }

    public abstract static class GenericBuilder {

        protected String serverCustomUrl = null;
        protected int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT_IN_MILLSECONDS;
        protected int readTimeout = DEFAULT_READ_TIMEOUT_IN_MILLSECONDS;
        protected int pollingTimeout = DEFAULT_POLLING_TIMEOUT_IN_MILLSECONDS;
        protected HttpServiceApi httpService;
        protected SSLContext sslContext;
        protected TransactionContext transactionContext;

        public GenericBuilder(SSLContext sslContext, FrejaEnvironment frejaEnvironment) throws FrejaEidClientInternalException {
            if (sslContext != null) {
                this.sslContext = sslContext;
            }
            serverCustomUrl = FrejaEnvironment.PRODUCTION.getUrl();
            if (frejaEnvironment == FrejaEnvironment.TEST) {
                serverCustomUrl = FrejaEnvironment.TEST.getUrl();
            }
        }

        public GenericBuilder(String keystorePath, String keystorePass, String certificatePath, FrejaEnvironment frejaEnvironment) throws FrejaEidClientInternalException {
            int numberOfFails = 0;
            for (KeyStoreType keyStoreType : KeyStoreType.values()) {
                try (InputStream keyStoreStream = new FileInputStream(keystorePath)) {
                    KeyStore keyStore = KeyStore.getInstance(keyStoreType.getType());
                    keyStore.load(keyStoreStream, keystorePass.toCharArray());
                    createSSLContext(keyStore, keystorePass, certificatePath);

                } catch (FrejaEidClientInternalException | IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
                    numberOfFails++;
                    continue;
                }
                break;
            }
            if (numberOfFails == KeyStoreType.values().length) {
                throw new FrejaEidClientInternalException(String.format("Failed to initiate SSL context with supported keystore types %s.", KeyStoreType.getAllKeyStoreTypes()));
            }
            serverCustomUrl = FrejaEnvironment.PRODUCTION.getUrl();
            if (frejaEnvironment == FrejaEnvironment.TEST) {
                serverCustomUrl = FrejaEnvironment.TEST.getUrl();
            }
        }

        private KeyStore createTrustStoreWithCertificate(String certificatePath) throws FrejaEidClientInternalException {
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

        private void createSSLContext(KeyStore keyStore, String keystorePass, String certificatePath) throws FrejaEidClientInternalException {
            try {
                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(keyStore, keystorePass.toCharArray());
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                if (certificatePath != null) {
                    keyStore = createTrustStoreWithCertificate(certificatePath);
                }
                tmf.init(keyStore);
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(keyManagerFactory.getKeyManagers(), tmf.getTrustManagers(), null);
                this.sslContext = sslContext;
            } catch (KeyManagementException | KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException ex) {
                throw new FrejaEidClientInternalException("Failed to create SSL context. ", ex);
            }
        }

        /**
         * Connection timeout is time to establish the connection with remote
         * host.
         *
         * @param connectionTimeout in milliseconds on client side. Default
         * value is {@value #DEFAULT_CONNECTION_TIMEOUT_IN_MILLSECONDS}
         * milliseconds.
         * @return clientBuilder
         */
        public GenericBuilder setConnectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        /**
         * Read timeout is time waiting for data after the connection was
         * established (maximum time of inactivity between two data packages).
         *
         * @param readTimeout in milliseconds on client side. Default value is
         * {@value #DEFAULT_READ_TIMEOUT_IN_MILLSECONDS} milliseconds.
         * @return clientBuilder
         */
        public GenericBuilder setReadTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        /**
         * Polling timeout is maximum time for waiting when polling for final
         * results.
         *
         * @param pollingTimeout in milliseconds on client side. Default value
         * is {@value #DEFAULT_POLLING_TIMEOUT_IN_MILLSECONDS} milliseconds.
         * @return clientBuilder
         */
        public GenericBuilder setPollingTimeout(int pollingTimeout) {
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
            this.transactionContext = transactionContext;
            return this;
        }

        public abstract <T extends BasicClient> T build() throws FrejaEidClientInternalException;

        protected void checkSetParametars() throws FrejaEidClientInternalException {
            if (pollingTimeout < 1000 || pollingTimeout > 30000) {
                throw new FrejaEidClientInternalException("Polling timeout must be between 1 and 30 seconds.");
            }
            if (transactionContext == null) {
                transactionContext = TransactionContext.PERSONAL;
            }
        }

    }

}
