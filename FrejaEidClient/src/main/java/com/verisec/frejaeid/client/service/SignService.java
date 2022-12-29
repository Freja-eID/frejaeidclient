package com.verisec.frejaeid.client.service;

import com.verisec.frejaeid.client.beans.common.EmptyFrejaResponse;
import com.verisec.frejaeid.client.beans.sign.cancel.CancelSignRequest;
import com.verisec.frejaeid.client.beans.sign.get.SignResultRequest;
import com.verisec.frejaeid.client.beans.sign.get.SignResult;
import com.verisec.frejaeid.client.beans.sign.get.SignResults;
import com.verisec.frejaeid.client.beans.sign.get.SignResultsRequest;
import com.verisec.frejaeid.client.beans.sign.init.InitiateSignRequest;
import com.verisec.frejaeid.client.beans.sign.init.InitiateSignResponse;
import com.verisec.frejaeid.client.enums.TransactionContext;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientPollingException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.http.HttpServiceApi;
import com.verisec.frejaeid.client.util.MethodUrl;
import com.verisec.frejaeid.client.util.RequestTemplate;

import java.util.concurrent.TimeUnit;

public class SignService extends BasicService {

    private final int pollingTimeoutInMilliseconds;
    private final TransactionContext transactionContext;

    public SignService(String serverAddress, int pollingTimeoutInMilliseconds, TransactionContext transactionContext,
                       HttpServiceApi httpService, String resourceServerAddress) {
        super(serverAddress, httpService, resourceServerAddress);
        this.pollingTimeoutInMilliseconds = pollingTimeoutInMilliseconds;
        this.transactionContext = transactionContext;
    }

    public SignService(String serverAddress, HttpServiceApi httpService, String resourceServerAddress) {
        super(serverAddress, httpService, resourceServerAddress);
        this.pollingTimeoutInMilliseconds = 0;
        this.transactionContext = TransactionContext.PERSONAL;

    }

    public InitiateSignResponse initiate(InitiateSignRequest signRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        MethodUrl methodUrl = transactionContext == TransactionContext.ORGANISATIONAL ?
                MethodUrl.ORGANISATION_SIGN_INIT : MethodUrl.SIGN_INIT;
        return httpService.send(getUrl(serverAddress, methodUrl), RequestTemplate.INIT_SIGN_TEMPLATE, signRequest,
                                InitiateSignResponse.class, signRequest.getRelyingPartyId());
    }

    public SignResult getResult(SignResultRequest signResultRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        MethodUrl methodUrl = transactionContext == TransactionContext.ORGANISATIONAL ?
                MethodUrl.ORGANISATION_SIGN_GET_ONE_RESULT : MethodUrl.SIGN_GET_RESULT;
        return httpService.send(getUrl(serverAddress, methodUrl), RequestTemplate.SIGN_RESULT_TEMPLATE,
                                signResultRequest, SignResult.class, signResultRequest.getRelyingPartyId());
    }

    public SignResult pollForResult(SignResultRequest signResultRequest, int maxWaitingTimeInSec)
            throws FrejaEidClientInternalException, FrejaEidException, FrejaEidClientPollingException {
        long pollingEndTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(maxWaitingTimeInSec);
        while (maxWaitingTimeInSec == 0
                || ((System.currentTimeMillis() + pollingTimeoutInMilliseconds) < pollingEndTime)) {
            SignResult getSignResult = getResult(signResultRequest);
            if (maxWaitingTimeInSec == 0 || isFinalStatus(getSignResult.getStatus())) {
                return getSignResult;
            }
            try {
                Thread.sleep(pollingTimeoutInMilliseconds);
            } catch (InterruptedException ex) {
                throw new FrejaEidClientInternalException(String.format(
                        "An error occurred while waiting to make another request with %ss polling timeout.",
                        maxWaitingTimeInSec), ex);
            }
        }
        throw new FrejaEidClientPollingException(
                String.format("A timeout of %ss was reached while sending request.", maxWaitingTimeInSec));
    }

    public SignResults getResults(SignResultsRequest signResultsRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        MethodUrl methodUrl = transactionContext == TransactionContext.ORGANISATIONAL ?
                MethodUrl.ORGANISATION_SIGN_GET_RESULTS : MethodUrl.SIGN_GET_RESULTS;
        return httpService.send(getUrl(serverAddress, methodUrl), RequestTemplate.SIGN_RESULTS_TEMPLATE,
                                signResultsRequest, SignResults.class, signResultsRequest.getRelyingPartyId());
    }

    public EmptyFrejaResponse cancel(CancelSignRequest cancelSignRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        MethodUrl methodUrl = transactionContext == TransactionContext.ORGANISATIONAL ?
                MethodUrl.ORGANISATION_SIGN_CANCEL : MethodUrl.SIGN_CANCEL;
        return httpService.send(getUrl(serverAddress, methodUrl), RequestTemplate.CANCEL_SIGN_TEMPLATE,
                                cancelSignRequest, EmptyFrejaResponse.class, cancelSignRequest.getRelyingPartyId());
    }

    public TransactionContext getTransactionContext() {
        return transactionContext;
    }
}
