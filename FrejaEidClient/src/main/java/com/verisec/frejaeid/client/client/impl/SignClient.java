package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.beans.general.SslSettings;
import com.verisec.frejaeid.client.beans.sign.cancel.CancelSignRequest;
import com.verisec.frejaeid.client.beans.sign.get.SignResultRequest;
import com.verisec.frejaeid.client.beans.sign.get.SignResult;
import com.verisec.frejaeid.client.beans.sign.get.SignResultsRequest;
import com.verisec.frejaeid.client.beans.sign.init.InitiateSignRequest;
import com.verisec.frejaeid.client.client.api.SignClientApi;
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
 * Performs sign actions.
 *
 */
public class SignClient extends BasicClient implements SignClientApi {
    
    public static final Logger LOG = LoggerFactory.getLogger(SignClient.class);
    private static final long DEFAULT_EXPIRY_TIME_IN_MILLIS = 120000;
    
    private SignClient(String serverCustomUrl, int pollingTimeoutInMillseconds, TransactionContext transactionContext, HttpServiceApi httpService) throws FrejaEidClientInternalException {
        super(serverCustomUrl, pollingTimeoutInMillseconds, transactionContext, httpService);
    }

    /**
     * SignClient should be initialized with keyStore parameters, server
     * certificate and type of environment.
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
    public String initiate(InitiateSignRequest initiateSignRequest) throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateInitSignRequest(initiateSignRequest, signService.getTransactionContext());
        LOG.debug("Initiating sign transaction for user info type {}, minimum registration level of user {}, requesting attributes {} and expiry time {} ms.", initiateSignRequest.getUserInfoType(), initiateSignRequest.getMinRegistrationLevel(),
                initiateSignRequest.getAttributesToReturn(), initiateSignRequest.getExpiry() == null ? DEFAULT_EXPIRY_TIME_IN_MILLIS : initiateSignRequest.getExpiry());
        String reference = signService.initiate(initiateSignRequest).getSignRef();
        LOG.debug("Received sign transaction reference {}.", reference);
        return reference;
    }
    
    @Override
    public SignResult getResult(SignResultRequest getOneSignResultRequest) throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateResultRequest(getOneSignResultRequest);
        LOG.debug("Getting result for sign transaction reference {}.", getOneSignResultRequest.getSignRef());
        SignResult signResult = signService.getResult(getOneSignResultRequest);
        LOG.debug("Received {} status for sign trasnsaction reference {}.", signResult.getStatus(), signResult.getSignRef());
        return signResult;
    }
    
    @Override
    public List<SignResult> getResults(SignResultsRequest getSignResultsRequest) throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateResultsRequest(getSignResultsRequest);
        LOG.debug("Getting all sign transaction results.");
        List<SignResult> signResults = signService.getResults(getSignResultsRequest).getSignatureResults();
        LOG.debug("Successfully received sign results.");
        return signResults;
    }
    
    @Override
    public SignResult pollForResult(SignResultRequest getOneSignResultRequest, int maxWaitingTimeInSec) throws FrejaEidClientInternalException, FrejaEidException, FrejaEidClientPollingException {
        requestValidationService.validateResultRequest(getOneSignResultRequest);
        LOG.debug("Polling {} s for result for sign transaction reference {}.", maxWaitingTimeInSec, getOneSignResultRequest.getSignRef());
        SignResult signResult = signService.pollForResult(getOneSignResultRequest, maxWaitingTimeInSec);
        LOG.debug("Received {} status for sign trasnsaction reference {}, after polling for result.", signResult.getStatus(), signResult.getSignRef());
        return signResult;
    }
    
    @Override
    public void cancel(CancelSignRequest cancelSignRequest) throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateCancelRequest(cancelSignRequest);
        LOG.debug("Canceling sign transaction with reference {}.", cancelSignRequest.getSignRef());
        signService.cancel(cancelSignRequest);
        LOG.debug("Successfully canceled sign transaction with reference {}.", cancelSignRequest.getSignRef());
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
        public SignClient build() throws FrejaEidClientInternalException {
            checkSetParameters();
            if (httpService == null) {
                httpService = new HttpService(sslContext, connectionTimeout, readTimeout);
            }
            LOG.debug("Successfully created SignClient with server url {}, polling timeout {} and transaction context {}.", serverCustomUrl, pollingTimeout, transactionContext);
            return new SignClient(serverCustomUrl, pollingTimeout, transactionContext, httpService);
        }
        
    }
    
}
