package com.verisec.frejaeid.client.client.api;

import com.verisec.frejaeid.client.beans.sign.cancel.CancelSignRequest;
import com.verisec.frejaeid.client.beans.sign.get.SignResultRequest;
import com.verisec.frejaeid.client.beans.sign.get.SignResult;
import com.verisec.frejaeid.client.beans.sign.get.SignResultsRequest;
import com.verisec.frejaeid.client.beans.sign.init.InitiateSignRequest;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientPollingException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.enums.TransactionStatus;
import java.util.List;

/**
 * Performs sign actions.
 *
 */
public interface SignClientApi {

    /**
     * Initiates a signing request. Transaction can be active between two
     * minutes and 30 days. Default value is two minutes and this can be changed
     * in request.
     *
     * @param initiateSignRequest instance of {@linkplain InitiateSignRequest}
     * with corresponding parameters.
     * @return sign transaction reference which is used for fetching sign
     * results.
     * @throws FrejaEidClientInternalException if internal validation of request
     * fails.
     * @throws FrejaEidException if server returns an error.
     */
    public String initiate(InitiateSignRequest initiateSignRequest) throws FrejaEidClientInternalException, FrejaEidException;

    /**
     * Fetches a single result for a specified signature transaction reference
     * (signRef returned from a call to initiate signing).
     *
     * @param signResultRequest contains transaction reference.
     * @return {@linkplain SignResult}
     * @throws FrejaEidClientInternalException if internal validation of request
     * fails.
     * @throws FrejaEidException if server returns an error.
     */
    public SignResult getResult(SignResultRequest signResultRequest) throws FrejaEidClientInternalException, FrejaEidException;

    /**
     * Fetches the results of multiple outstanding signature requests.
     *
     * @param signResultsRequest will request all sign actions.
     * @return a complete list of sign actions, successfully initiated within
     * last 3 days.
     * @throws FrejaEidClientInternalException if internal validation of request
     * fails.
     * @throws FrejaEidException if server returns an error.
     */
    public List<SignResult> getResults(SignResultsRequest signResultsRequest) throws FrejaEidClientInternalException, FrejaEidException;

    /**
     * Blocking method which is used to fetch a single result with the final
     * {@linkplain TransactionStatus}.
     *
     * @param signResultRequest contains transaction reference.
     * @param maxWaitingTimeInSec is a maximum time in seconds to wait for a
     * final TransactionStatus.
     * @return {@linkplain SignResult}
     *
     * @throws FrejaEidClientInternalException if internal validation of request
     * fails.
     * @throws FrejaEidException if server returns an error.
     * @throws FrejaEidClientPollingException if the maximum polling time
     * expires before the action is completed.
     */
    public SignResult pollForResult(SignResultRequest signResultRequest, int maxWaitingTimeInSec) throws FrejaEidClientInternalException, FrejaEidException, FrejaEidClientPollingException;

    /**
     * Cancels an initiated sign request.
     *
     * @param cancelSignRequest contains transaction reference.
     * @throws FrejaEidClientInternalException if internal validation of request
     * fails.
     * @throws FrejaEidException if server returns an error.
     */
    public void cancel(CancelSignRequest cancelSignRequest) throws FrejaEidClientInternalException, FrejaEidException;

}
