package com.verisec.frejaeid.client.service;

import com.verisec.frejaeid.client.beans.custodianship.get.GetUserCustodianshipStatusRequest;
import com.verisec.frejaeid.client.beans.custodianship.get.GetUserCustodianshipStatusResponse;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.http.HttpServiceApi;
import com.verisec.frejaeid.client.util.MethodUrl;
import com.verisec.frejaeid.client.util.RequestTemplate;

public class CustodianshipService extends BasicService {
    public CustodianshipService(String serverAddress, HttpServiceApi httpService, String resourceServiceUrl) {
        super(serverAddress, httpService, resourceServiceUrl);
    }

    public GetUserCustodianshipStatusResponse getUserCustodianshipStatus(
            GetUserCustodianshipStatusRequest getUserCustodianshipStatusRequest) throws FrejaEidClientInternalException, FrejaEidException {
        return httpService.send(getUrl(serverAddress, MethodUrl.CUSTODIANSHIP_GET_USER_STATUS),
                                RequestTemplate.GET_CUSTODIANSHIP_STATUS_TEMPLATE, getUserCustodianshipStatusRequest,
                                GetUserCustodianshipStatusResponse.class,
                                getUserCustodianshipStatusRequest.getRelyingPartyId());
    }
}
