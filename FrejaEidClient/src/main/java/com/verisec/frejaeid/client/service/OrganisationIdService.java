package com.verisec.frejaeid.client.service;

import com.verisec.frejaeid.client.beans.common.EmptyFrejaResponse;
import com.verisec.frejaeid.client.beans.organisationid.cancel.CancelAddOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.delete.DeleteOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.get.OrganisationIdResultRequest;
import com.verisec.frejaeid.client.beans.organisationid.get.OrganisationIdResult;
import com.verisec.frejaeid.client.beans.organisationid.getall.GetAllOrganisationIdUsersRequest;
import com.verisec.frejaeid.client.beans.organisationid.getall.GetAllOrganisationIdUsersResponse;
import com.verisec.frejaeid.client.beans.organisationid.init.InitiateAddOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.init.InitiateAddOrganisationIdResponse;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientPollingException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.http.HttpServiceApi;
import com.verisec.frejaeid.client.util.MethodUrl;
import com.verisec.frejaeid.client.util.RequestTemplate;
import java.util.concurrent.TimeUnit;

public class OrganisationIdService extends BasicService {

    private final int pollingTimeoutInMilliseconds;

    public OrganisationIdService(String serverAddress, int pollingTimeoutInMilliseconds, HttpServiceApi httpService) throws FrejaEidClientInternalException {
        super(serverAddress, httpService);
        this.pollingTimeoutInMilliseconds = pollingTimeoutInMilliseconds;
    }

    public OrganisationIdService(String serverAddress, HttpServiceApi httpService) throws FrejaEidClientInternalException {
        super(serverAddress, httpService);
        this.pollingTimeoutInMilliseconds = 0;
    }

    public InitiateAddOrganisationIdResponse initiateAdd(InitiateAddOrganisationIdRequest initiateAddOrganisaitonIdRequest) throws FrejaEidClientInternalException, FrejaEidException {
        return httpService.send(getUrl(serverAddress, MethodUrl.ORGANISATION_ID_INIT_ADD), RequestTemplate.INIT_ADD_ORGANISATION_ID_TEMPLATE, initiateAddOrganisaitonIdRequest, InitiateAddOrganisationIdResponse.class, initiateAddOrganisaitonIdRequest.getRelyingPartyId());
    }

    public OrganisationIdResult getResult(OrganisationIdResultRequest organisationIdResultRequest) throws FrejaEidClientInternalException, FrejaEidException {
        return httpService.send(getUrl(serverAddress, MethodUrl.ORGANISATION_ID_GET_RESULT), RequestTemplate.ORGANISATION_ID_RESULT_TEMPLATE, organisationIdResultRequest, OrganisationIdResult.class, organisationIdResultRequest.getRelyingPartyId());
    }

    public OrganisationIdResult pollForResult(OrganisationIdResultRequest organisationIdResultRequest, int maxWaitingTimeInSec) throws FrejaEidClientInternalException, FrejaEidException, FrejaEidClientPollingException {
        long pollingEndTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(maxWaitingTimeInSec);
        while (maxWaitingTimeInSec == 0 || ((System.currentTimeMillis() + pollingTimeoutInMilliseconds) < pollingEndTime)) {
            OrganisationIdResult getOrganisationIdResult = getResult(organisationIdResultRequest);
            if (maxWaitingTimeInSec == 0 || isFinalStatus(getOrganisationIdResult.getStatus())) {
                return getOrganisationIdResult;
            }
            try {
                Thread.sleep(pollingTimeoutInMilliseconds);
            } catch (InterruptedException ex) {
                throw new FrejaEidClientInternalException(String.format("An error occured while waiting to make another request with %ss polling timeout.", maxWaitingTimeInSec), ex);
            }
        }
        throw new FrejaEidClientPollingException(String.format("A timeout of %ss was reached while sending request.", maxWaitingTimeInSec));
    }

    public EmptyFrejaResponse delete(DeleteOrganisationIdRequest deleteOrganisationIdRequest) throws FrejaEidClientInternalException, FrejaEidException {
        return httpService.send(getUrl(serverAddress, MethodUrl.ORGANISATION_ID_DELETE), RequestTemplate.DELETE_ORGANINSATION_ID_TEMPLATE, deleteOrganisationIdRequest, EmptyFrejaResponse.class, deleteOrganisationIdRequest.getRelyingPartyId());
    }

    public EmptyFrejaResponse cancelAdd(CancelAddOrganisationIdRequest cancelAddOrganisationIdRequest) throws FrejaEidClientInternalException, FrejaEidException {
        return httpService.send(getUrl(serverAddress, MethodUrl.ORGANISATION_ID_CANCEL_ADD), RequestTemplate.CANCEL_ADD_ORGANISATION_ID_TEMPLATE, cancelAddOrganisationIdRequest, EmptyFrejaResponse.class, cancelAddOrganisationIdRequest.getRelyingPartyId());
    }
    
    public GetAllOrganisationIdUsersResponse getAllUsers(GetAllOrganisationIdUsersRequest getAllOrganisationIdUsersRequest) throws FrejaEidClientInternalException, FrejaEidException {
        return httpService.send(getUrl(serverAddress, MethodUrl.ORGANISATION_ID_GET_ALL_USERS), RequestTemplate.GET_ALL_ORGANISATION_ID_USERS_TEMPLATE, getAllOrganisationIdUsersRequest, GetAllOrganisationIdUsersResponse.class, getAllOrganisationIdUsersRequest.getRelyingPartyId());
    }
}
