package com.verisec.frejaeid.client.client.api;

import com.verisec.frejaeid.client.beans.authentication.cancel.CancelAuthenticationRequest;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResultsRequest;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResultRequest;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResult;
import com.verisec.frejaeid.client.beans.authentication.init.InitiateAuthenticationRequest;
import com.verisec.frejaeid.client.beans.authentication.init.InitiateAuthenticationResponse;
import com.verisec.frejaeid.client.enums.TransactionStatus;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientPollingException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;

import java.io.IOException;
import java.util.List;

/**
 * Performs authentication actions.
 */
public interface AuthenticationClientApi {

    /**
     * Initiates authentication transaction. It will be active for two minutes.
     *
     * @param initiateAuthenticationRequest instance of
     *                                      {@linkplain InitiateAuthenticationRequest} with corresponding parameters.
     * @return {@linkplain InitiateAuthenticationResponse}
     * @throws FrejaEidClientInternalException if internal validation of request
     *                                         fails.
     * @throws FrejaEidException               if server returns an error.
     */
    public InitiateAuthenticationResponse initiate(InitiateAuthenticationRequest initiateAuthenticationRequest)
            throws FrejaEidClientInternalException, FrejaEidException;

    /**
     * Fetches a single result for a specified unique transaction reference
     * provided by server as a result of initiating transaction.
     *
     * @param authenticationResultRequest contains transaction reference.
     * @return {@linkplain AuthenticationResult}
     * @throws FrejaEidClientInternalException if internal validation of request
     *                                         fails.
     * @throws FrejaEidException               if server returns an error.
     */
    public AuthenticationResult getResult(AuthenticationResultRequest authenticationResultRequest)
            throws FrejaEidClientInternalException, FrejaEidException;

    /**
     * Fetches the results of multiple outstanding authentications.
     *
     * @param authenticationResultsRequest will request all authentications
     * @return a complete list of authentications, successfully initiated within
     * last 10 minutes.
     * @throws FrejaEidClientInternalException if internal validation of request
     *                                         fails.
     * @throws FrejaEidException               if server returns an error.
     */
    public List<AuthenticationResult> getResults(AuthenticationResultsRequest authenticationResultsRequest)
            throws FrejaEidClientInternalException, FrejaEidException;

    /**
     * Blocking method which is used to fetch a single result with the final
     * {@linkplain TransactionStatus}.
     *
     * @param authenticationResultRequest contains transaction reference.
     * @param maxWaitingTimeInSec         is a maximum time in seconds to wait for a
     *                                    final TransactionStatus.
     * @return {@linkplain AuthenticationResult}
     * @throws FrejaEidClientInternalException if internal validation of request
     *                                         fails.
     * @throws FrejaEidException               if server returns an error.
     * @throws FrejaEidClientPollingException  if the maximum polling time
     *                                         expires before the action is completed.
     */
    public AuthenticationResult pollForResult(AuthenticationResultRequest authenticationResultRequest,
                                              int maxWaitingTimeInSec)
            throws FrejaEidClientInternalException, FrejaEidException, FrejaEidClientPollingException;

    /**
     * Cancels an initiated authentication request.
     *
     * @param cancelAuthenticationRequest contains transaction reference.
     * @throws FrejaEidClientInternalException if internal validation of request
     *                                         fails.
     * @throws FrejaEidException               if server returns an error.
     */
    public void cancel(CancelAuthenticationRequest cancelAuthenticationRequest)
            throws FrejaEidClientInternalException, FrejaEidException;

    /**
     * Return QR code for authentication in a byte array.
     *
     * @param reference contains transaction reference.
     */
    public byte[] generateQRCodeForAuthentication(String reference)
            throws FrejaEidClientInternalException, FrejaEidException, IOException;
}
