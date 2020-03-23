package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.beans.authentication.cancel.CancelAuthenticationRequest;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResultsRequest;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResultRequest;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResult;
import com.verisec.frejaeid.client.beans.authentication.init.InitiateAuthenticationRequest;
import com.verisec.frejaeid.client.beans.general.SslSettings;
import com.verisec.frejaeid.client.client.api.AuthenticationClientApi;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.enums.TransactionContext;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientPollingException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.http.HttpService;
import com.verisec.frejaeid.client.http.HttpServiceApi;
import java.util.List;
import javax.net.ssl.SSLContext;

/**
 * Performs authentication actions.
 *
 */
public class AuthenticationClient extends BasicClient implements AuthenticationClientApi {

    private AuthenticationClient(String serverCustomUrl, int pollingTimeoutInMillseconds, TransactionContext transactionContext, HttpServiceApi httpService) throws FrejaEidClientInternalException {
        super(serverCustomUrl, pollingTimeoutInMillseconds, transactionContext, httpService);
    }

    /**
     * AuthenticationClient should be initialized with keyStore parameters,
     * server certificate and type of environment.
     *
     * @param sslSettings instance of wrapper class {@link SslSettings}
     * @param frejaEnvironment determines which {@linkplain FrejaEnvironment}
     * will be used
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
    public String initiate(InitiateAuthenticationRequest initiateAuthenticationRequest) throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateInitAuthRequest(initiateAuthenticationRequest, authenticationService.getTransactionContext());
        return authenticationService.initiate(initiateAuthenticationRequest).getAuthRef();
    }

    @Override
    public AuthenticationResult getResult(AuthenticationResultRequest authenticationResultRequest) throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateResultRequest(authenticationResultRequest);
        return authenticationService.getResult(authenticationResultRequest);
    }

    @Override
    public List<AuthenticationResult> getResults(AuthenticationResultsRequest authenticationResultsRequest) throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateResultsRequest(authenticationResultsRequest);
        return authenticationService.getResults(authenticationResultsRequest).getAuthenticationResults();
    }

    @Override
    public AuthenticationResult pollForResult(AuthenticationResultRequest authenticationResultRequest, int maxWaitingTimeInSec) throws FrejaEidClientInternalException, FrejaEidException, FrejaEidClientPollingException {
        requestValidationService.validateResultRequest(authenticationResultRequest);
        return authenticationService.pollForResult(authenticationResultRequest, maxWaitingTimeInSec);
    }

    @Override
    public void cancel(CancelAuthenticationRequest cancelAuthenticationRequest) throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateCancelRequest(cancelAuthenticationRequest);
        authenticationService.cancel(cancelAuthenticationRequest);
    }

    public static class Builder extends GenericBuilder {

        private Builder(SSLContext sslContext, FrejaEnvironment frejaEnvironment) throws FrejaEidClientInternalException {
            super(sslContext, frejaEnvironment);
        }

        private Builder(String keystorePath, String keystorePass, String certificatePath, FrejaEnvironment frejaEnvironment) throws FrejaEidClientInternalException {
            super(keystorePath, keystorePass, certificatePath, frejaEnvironment);
        }

        @Override
        public AuthenticationClient build() throws FrejaEidClientInternalException {
            checkSetParameters();
            if (httpService == null) {
                httpService = new HttpService(sslContext, connectionTimeout, readTimeout);
            }
            return new AuthenticationClient(serverCustomUrl, pollingTimeout, transactionContext, httpService);
        }

    }
}
