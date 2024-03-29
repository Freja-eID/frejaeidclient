package com.verisec.frejaeid.client.service;

import com.verisec.frejaeid.client.beans.common.EmptyFrejaResponse;
import com.verisec.frejaeid.client.beans.authentication.cancel.CancelAuthenticationRequest;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResultsRequest;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResults;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResultRequest;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResult;
import com.verisec.frejaeid.client.beans.authentication.init.InitiateAuthenticationResponse;
import com.verisec.frejaeid.client.beans.authentication.init.InitiateAuthenticationRequest;
import com.verisec.frejaeid.client.enums.TransactionContext;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientPollingException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.http.HttpServiceApi;
import com.verisec.frejaeid.client.util.MethodUrl;
import com.verisec.frejaeid.client.util.RequestTemplate;

import java.util.concurrent.TimeUnit;

public class AuthenticationService extends BasicService {

    private final int pollingTimeoutInMilliseconds;
    private final TransactionContext transactionContext;

    public AuthenticationService(String serverAddress, HttpServiceApi httpService, int pollingTimeoutInMilliseconds,
                                 TransactionContext transactionContext, String resourceServiceUrl) {
        super(serverAddress, httpService, resourceServiceUrl);
        this.pollingTimeoutInMilliseconds = pollingTimeoutInMilliseconds;
        this.transactionContext = transactionContext;
    }

    public AuthenticationService(String serverAddress, HttpServiceApi httpService, String resourceServiceUrl) {
        super(serverAddress, httpService, resourceServiceUrl);
        this.pollingTimeoutInMilliseconds = 0;
        this.transactionContext = TransactionContext.PERSONAL;
    }

    public InitiateAuthenticationResponse initiate(InitiateAuthenticationRequest initiateAuthenticationRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        MethodUrl methodUrl = transactionContext == TransactionContext.ORGANISATIONAL ?
                MethodUrl.ORGANISATION_AUTHENTICATION_INIT : MethodUrl.AUTHENTICATION_INIT;
        return httpService.send(getUrl(serverAddress, methodUrl), RequestTemplate.INIT_AUTHENTICATION,
                                initiateAuthenticationRequest, InitiateAuthenticationResponse.class,
                                initiateAuthenticationRequest.getRelyingPartyId());
    }

    public AuthenticationResult getResult(AuthenticationResultRequest authenticationResultRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        MethodUrl methodUrl = transactionContext == TransactionContext.ORGANISATIONAL ?
                MethodUrl.ORGANISATION_AUTHENTICATION_GET_ONE_RESULT : MethodUrl.AUTHENTICATION_GET_RESULT;
        return httpService.send(getUrl(serverAddress, methodUrl), RequestTemplate.AUTHENTICATION_RESULT_TEMPLATE,
                                authenticationResultRequest, AuthenticationResult.class,
                                authenticationResultRequest.getRelyingPartyId());
    }

    public AuthenticationResult pollForResult(AuthenticationResultRequest authenticationResultRequest,
                                              int maxWaitingTimeInSec)
            throws FrejaEidClientInternalException, FrejaEidException, FrejaEidClientPollingException {
        long pollingEndTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(maxWaitingTimeInSec);
        while (maxWaitingTimeInSec == 0 || isPollingTimeExpired(pollingEndTime)) {
            AuthenticationResult getAuthenticationResult = getResult(authenticationResultRequest);
            if (maxWaitingTimeInSec == 0 || isFinalStatus(getAuthenticationResult.getStatus())) {
                return getAuthenticationResult;
            }
            try {
                Thread.sleep(pollingTimeoutInMilliseconds);
            } catch (InterruptedException ex) {
                throw new FrejaEidClientInternalException(
                        String.format("An error occured while waiting to make another request with %ss polling " +
                                              "timeout.", maxWaitingTimeInSec), ex);
            }
        }
        throw new FrejaEidClientPollingException(
                String.format("A timeout of %ss was reached while sending request.", maxWaitingTimeInSec));
    }

    public AuthenticationResults getResults(AuthenticationResultsRequest authenticationResultsRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        MethodUrl methodUrl = transactionContext == TransactionContext.ORGANISATIONAL ?
                MethodUrl.ORGANISATION_AUTHENTICATION_GET_RESULTS : MethodUrl.AUTHENTICATION_GET_RESULTS;
        return httpService.send(getUrl(serverAddress, methodUrl), RequestTemplate.AUTHENTICATION_RESULTS_TEMPLATE,
                                authenticationResultsRequest, AuthenticationResults.class,
                                authenticationResultsRequest.getRelyingPartyId());
    }

    public EmptyFrejaResponse cancel(CancelAuthenticationRequest cancelAuthenticationRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        MethodUrl methodUrl = transactionContext == TransactionContext.ORGANISATIONAL ?
                MethodUrl.ORGANISATION_AUTHENTICATION_CANCEL : MethodUrl.AUTHENTICATION_CANCEL;
        return httpService.send(getUrl(serverAddress, methodUrl), RequestTemplate.CANCEL_AUTHENTICATION_TEMPLATE,
                                cancelAuthenticationRequest, EmptyFrejaResponse.class,
                                cancelAuthenticationRequest.getRelyingPartyId());
    }

    public TransactionContext getTransactionContext() {
        return transactionContext;
    }

    private boolean isPollingTimeExpired(long pollingEndTime) {
        return (System.currentTimeMillis() + pollingTimeoutInMilliseconds) < pollingEndTime;
    }

}
