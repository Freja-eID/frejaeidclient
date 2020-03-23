package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.beans.general.SslSettings;
import com.verisec.frejaeid.client.beans.usermanagement.customidentifier.delete.DeleteCustomIdentifierRequest;
import com.verisec.frejaeid.client.beans.usermanagement.customidentifier.set.SetCustomIdentifierRequest;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.http.HttpService;
import com.verisec.frejaeid.client.http.HttpServiceApi;
import javax.net.ssl.SSLContext;
import com.verisec.frejaeid.client.client.api.CustomIdentifierClientApi;
import com.verisec.frejaeid.client.enums.TransactionContext;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;

/**
 * Performs actions with custom identifier.
 *
 */
public class CustomIdentifierClient extends BasicClient implements CustomIdentifierClientApi {

    private CustomIdentifierClient(String serverCustomUrl, int pollingTimeout, HttpServiceApi httpService) throws FrejaEidClientInternalException {
        super(serverCustomUrl, pollingTimeout, TransactionContext.PERSONAL, httpService);
    }

    /**
     * CustomIdentifierClient should be initialized with keyStore parameters,
     * server certificate and type of environment.
     *
     * @param sslSettings instance of wrapper class {@link SslSettings}
     * @param frejaEnvironment determinate which environment will be used and
     * can be {@linkplain FrejaEnvironment#TEST}, if you want to perform some
     * tests first or {@linkplain FrejaEnvironment#PRODUCTION} if you want to
     * send requests to production.
     *
     * @return client builder
     * @throws FrejaEidClientInternalException if fails to initiate SSL context
     * with given parameters(wrong password, wrong absolute path or unsupported
     * type of client keyStore or server certificate etc.).
     */
    public static Builder create(SslSettings sslSettings, FrejaEnvironment frejaEnvironment) throws FrejaEidClientInternalException {
        if (sslSettings == null) {
            throw new FrejaEidClientInternalException("SslSettings cannot be null.");
        }
        if (sslSettings.getSslContext() == null) {
            return new Builder(sslSettings.getKeystorePath(), sslSettings.getKeystorePass(), sslSettings.getServerCertificatePath(), frejaEnvironment);
        }
        return new Builder(sslSettings.getSslContext(), frejaEnvironment);
    }

    @Override
    public void set(SetCustomIdentifierRequest setCustomIdentifierRequest) throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateSetCustomIDentifierRequest(setCustomIdentifierRequest);
        customIdentifierService.set(setCustomIdentifierRequest);
    }

    @Override
    public void delete(DeleteCustomIdentifierRequest deleteCustomIdentifierRequest) throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateDeleteCustomIdentifierRequest(deleteCustomIdentifierRequest);
        customIdentifierService.delete(deleteCustomIdentifierRequest);
    }

    public static class Builder extends GenericBuilder {

        private Builder(SSLContext sslContext, FrejaEnvironment frejaEnvironment) throws FrejaEidClientInternalException {
            super(sslContext, frejaEnvironment);
        }

        private Builder(String keystorePath, String keystorePass, String certificatePath, FrejaEnvironment frejaEnvironment) throws FrejaEidClientInternalException {
            super(keystorePath, keystorePass, certificatePath, frejaEnvironment);
        }

        @Override
        public CustomIdentifierClient build() throws FrejaEidClientInternalException {
            transactionContext = TransactionContext.PERSONAL;
            checkSetParameters();
            if (httpService == null) {
                httpService = new HttpService(sslContext, connectionTimeout, readTimeout);
            }
            return new CustomIdentifierClient(serverCustomUrl, pollingTimeout, httpService);
        }

    }

}
