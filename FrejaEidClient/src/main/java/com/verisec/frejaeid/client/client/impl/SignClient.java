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

/**
 * Performs sign actions.
 *
 */
public class SignClient extends BasicClient implements SignClientApi {

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
        if (sslSettings.getSslContext() == null) {
            return new Builder(sslSettings.getKeystorePath(), sslSettings.getKeystorePass(), sslSettings.getServerCertificatePath(), frejaEnvironment);
        }
        return new Builder(sslSettings.getSslContext(), frejaEnvironment);
    }

    @Override
    public String initiate(InitiateSignRequest initiateSignRequest) throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateInitSignRequest(initiateSignRequest, signService.getTransactionContext());
        return signService.initiate(initiateSignRequest).getSignRef();
    }

    @Override
    public SignResult getResult(SignResultRequest getOneSignResultRequest) throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateResultRequest(getOneSignResultRequest);
        return signService.getResult(getOneSignResultRequest);
    }

    @Override
    public List<SignResult> getResults(SignResultsRequest getSignResultsRequest) throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateResultsRequest(getSignResultsRequest);
        return signService.getResults(getSignResultsRequest).getSignatureResults();
    }

    @Override
    public SignResult pollForResult(SignResultRequest getOneSignResultRequest, int maxWaitingTimeInSec) throws FrejaEidClientInternalException, FrejaEidException, FrejaEidClientPollingException {
        requestValidationService.validateResultRequest(getOneSignResultRequest);
        return signService.pollForResult(getOneSignResultRequest, maxWaitingTimeInSec);
    }

    @Override
    public void cancel(CancelSignRequest cancelSignRequest) throws FrejaEidClientInternalException, FrejaEidException {
        requestValidationService.validateCancelRequest(cancelSignRequest);
        signService.cancel(cancelSignRequest);
    }

    public static class Builder extends GenericBuilder {

        private Builder(SSLContext sslContext, FrejaEnvironment frejaEnvironment) throws FrejaEidClientInternalException {
            super(sslContext, frejaEnvironment);
        }

        private Builder(String keystorePath, String keystorePass, String certificatePath, FrejaEnvironment frejaEnvironment) throws FrejaEidClientInternalException {
            super(keystorePath, keystorePass, certificatePath, frejaEnvironment);
        }

        @Override
        public SignClient build() throws FrejaEidClientInternalException {
            checkSetParametars();
            if (httpService == null) {
                httpService = new HttpService(sslContext, connectionTimeout, readTimeout);
            }
            return new SignClient(serverCustomUrl, pollingTimeout, transactionContext, httpService);
        }

    }

}
