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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Performs authentication actions.
 *
 */
public class AuthenticationClient extends BasicClient implements AuthenticationClientApi {
    
    public static final Logger LOG = LoggerFactory.getLogger(AuthenticationClient.class);
    
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
        LOG.debug("Initiating authentication transaction for user info type {}, minimum registration level of user {} and requesting attributes {}.", initiateAuthenticationRequest.getUserInfoType(), initiateAuthenticationRequest.getMinRegistrationLevel().getState(), initiateAuthenticationRequest.getAttributesToReturn());
        String reference = authenticationService.initiate(initiateAuthenticationRequest).getAuthRef();
        LOG.debug("Received authentication transaction reference {}.", reference);
        return reference;
    }
    
    @Override
    public AuthenticationResult getResult(AuthenticationResultRequest authenticationResultRequest) throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateResultRequest(authenticationResultRequest);
        LOG.debug("Getting result for authentication transaction reference {}.", authenticationResultRequest.getAuthRef());
        AuthenticationResult authenticationResult = authenticationService.getResult(authenticationResultRequest);
        LOG.debug("Received {} status for authentication transaction reference {}.", authenticationResult.getStatus(), authenticationResult.getAuthRef());
        return authenticationResult;
    }
    
    @Override
    public List<AuthenticationResult> getResults(AuthenticationResultsRequest authenticationResultsRequest) throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateResultsRequest(authenticationResultsRequest);
        LOG.debug("Getting all authentication transaction results.");
        List<AuthenticationResult> authenticationResults = authenticationService.getResults(authenticationResultsRequest).getAuthenticationResults();
        LOG.debug("Successfully received authentication transaction results.");
        return authenticationResults;
    }
    
    @Override
    public AuthenticationResult pollForResult(AuthenticationResultRequest authenticationResultRequest, int maxWaitingTimeInSec) throws FrejaEidClientInternalException, FrejaEidException, FrejaEidClientPollingException {
        requestValidationService.validateResultRequest(authenticationResultRequest);
        LOG.debug("Polling {}s for result for authentication transaction reference {}.", maxWaitingTimeInSec, authenticationResultRequest.getAuthRef());
        AuthenticationResult authenticationResult = authenticationService.pollForResult(authenticationResultRequest, maxWaitingTimeInSec);
        LOG.debug("Received {} status for authentication transaction reference {}, after polling for result.", authenticationResult.getStatus(), authenticationResult.getAuthRef());
        return authenticationResult;
    }
    
    @Override
    public void cancel(CancelAuthenticationRequest cancelAuthenticationRequest) throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateCancelRequest(cancelAuthenticationRequest);
        LOG.debug("Canceling authentication transaction with reference {}.", cancelAuthenticationRequest.getAuthRef());
        authenticationService.cancel(cancelAuthenticationRequest);
        LOG.debug("Successfully canceled authentication transaction with reference {}.", cancelAuthenticationRequest.getAuthRef());
    }
    
    public static class Builder extends GenericBuilder {
        
        public static final Logger LOG = LoggerFactory.getLogger(Builder.class);
        
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
            LOG.debug("Successfully created AuthenticationClient with server URL {}, polling timeout {}ms and transaction context {}.", serverCustomUrl, pollingTimeout, transactionContext.getContext());
            return new AuthenticationClient(serverCustomUrl, pollingTimeout, transactionContext, httpService);
        }
        
    }
}
