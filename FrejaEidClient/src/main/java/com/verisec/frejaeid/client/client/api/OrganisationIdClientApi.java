package com.verisec.frejaeid.client.client.api;

import com.verisec.frejaeid.client.beans.general.OrganisationIdUserInfo;
import com.verisec.frejaeid.client.beans.organisationid.cancel.CancelAddOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.delete.DeleteOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.get.OrganisationIdResult;
import com.verisec.frejaeid.client.beans.organisationid.get.OrganisationIdResultRequest;
import com.verisec.frejaeid.client.beans.organisationid.getall.GetAllOrganisationIdUsersRequest;
import com.verisec.frejaeid.client.beans.organisationid.init.InitiateAddOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.update.UpdateOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.update.UpdateOrganisationIdResponse;
import com.verisec.frejaeid.client.enums.TransactionStatus;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientPollingException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;

import java.util.List;

/**
 * Performs actions with organisation eID.
 */
public interface OrganisationIdClientApi {

    /**
     * Initiates adding organisation id for a specific person.
     *
     * @param initiateAddOrganisationIdRequest instance of
     *                                         {@linkplain InitiateAddOrganisationIdRequest} with corresponding
     *                                         parameters
     * @return transaction reference which is used for fetching results.
     * @throws FrejaEidClientInternalException if internal validation of request
     *                                         fails.
     * @throws FrejaEidException               if server returns an error.
     */
    public String initiateAdd(InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest)
            throws FrejaEidClientInternalException, FrejaEidException;

    /**
     * Fetches a single result for a specified unique transaction reference
     * provided by server as a result of
     * {@linkplain #initiateAdd(InitiateAddOrganisationIdRequest)} method.
     *
     * @param organisationIdResultRequest contains transaction reference.
     * @return {@linkplain OrganisationIdResult}
     * @throws FrejaEidClientInternalException if internal validation of request
     *                                         fails.
     * @throws FrejaEidException               if server returns an error.
     */
    public OrganisationIdResult getResult(OrganisationIdResultRequest organisationIdResultRequest)
            throws FrejaEidClientInternalException, FrejaEidException;

    /**
     * Blocking method which is used to fetch a single result with the final
     * {@linkplain TransactionStatus}.
     *
     * @param organisationIdResultRequest contains transaction reference.
     * @param maxWaitingTimeInSec         is a maximum time in seconds to wait for a
     *                                    final TransactionStatus.
     * @return {@linkplain OrganisationIdResult}
     * @throws FrejaEidClientInternalException if internal validation of request
     *                                         fails.
     * @throws FrejaEidException               if server returns an error.
     * @throws FrejaEidClientPollingException  if the maximum polling time
     *                                         expires before the action is completed.
     */
    public OrganisationIdResult pollForResult(OrganisationIdResultRequest organisationIdResultRequest,
                                              int maxWaitingTimeInSec)
            throws FrejaEidClientInternalException, FrejaEidException, FrejaEidClientPollingException;

    /**
     * Cancels an initiated organisation id request.
     *
     * @param cancelAddOrganisationIdRequest contains add organisation id
     *                                       transaction reference.
     * @throws FrejaEidClientInternalException if internal validation of request
     *                                         fails.
     * @throws FrejaEidException               if server returns an error.
     */
    public void cancelAdd(CancelAddOrganisationIdRequest cancelAddOrganisationIdRequest)
            throws FrejaEidClientInternalException, FrejaEidException;

    /**
     * Deletes organisation id from Freja eID.
     *
     * @param deleteOrganisationIdRequest contains organisation id that will be
     *                                    deleted from Freja eID.
     * @throws FrejaEidClientInternalException if internal validation of request
     *                                         fails.
     * @throws FrejaEidException               if server returns an error.
     */
    public void delete(DeleteOrganisationIdRequest deleteOrganisationIdRequest)
            throws FrejaEidClientInternalException, FrejaEidException;

    /**
     * Gets information about users which have Organisation ID.
     *
     * @param getAllOrganisationIdUsersRequest
     * @return list of {@linkplain OrganisationIdUserInfo}
     * @throws FrejaEidClientInternalException if internal validation of request
     *                                         fails.
     * @throws FrejaEidException               if server returns an error.
     */
    public List<OrganisationIdUserInfo> getAllUsers(GetAllOrganisationIdUsersRequest getAllOrganisationIdUsersRequest)
            throws FrejaEidClientInternalException, FrejaEidException;

    /**
     * Gets information about users for whom relying party has set Organisation ID (all registration states included).
     *
     * @param getAllOrganisationIdUsersRequest
     * @return list of {@linkplain OrganisationIdUserInfo}
     * @throws FrejaEidClientInternalException if internal validation of request
     *                                         fails.
     * @throws FrejaEidException               if server returns an error.
     */
    public List<OrganisationIdUserInfo> getAllUsersV1_1(GetAllOrganisationIdUsersRequest getAllOrganisationIdUsersRequest)
            throws FrejaEidClientInternalException, FrejaEidException;

    /**
     * Updates issued organisation id for a specific person.
     *
     * @param updateOrganisationIdRequest instance of
     *                                         {@linkplain UpdateOrganisationIdRequest} with corresponding
     *                                         parameters
     * @return {@linkplain UpdateOrganisationIdResponse}
     * @throws FrejaEidClientInternalException if internal validation of request
     *                                         fails.
     * @throws FrejaEidException               if server returns an error.
     */
    public UpdateOrganisationIdResponse update(UpdateOrganisationIdRequest updateOrganisationIdRequest)
            throws FrejaEidClientInternalException, FrejaEidException;

}
