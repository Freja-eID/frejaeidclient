package com.verisec.frejaeid.client.service;

import com.verisec.frejaeid.client.beans.common.EmptyFrejaResponse;
import com.verisec.frejaeid.client.beans.usermanagement.customidentifier.delete.DeleteCustomIdentifierRequest;
import com.verisec.frejaeid.client.beans.usermanagement.customidentifier.set.SetCustomIdentifierRequest;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.http.HttpServiceApi;
import com.verisec.frejaeid.client.util.MethodUrl;
import com.verisec.frejaeid.client.util.RequestTemplate;

public class CustomIdentifierService extends BasicService {

    public CustomIdentifierService(String serverAddress, HttpServiceApi httpService, String resourceServiceUrl) {
        super(serverAddress, httpService, resourceServiceUrl);
    }

    public void set(SetCustomIdentifierRequest customIdentifierRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        httpService.send(getUrl(serverAddress, MethodUrl.CUSTOM_IDENTIFIER_SET),
                         RequestTemplate.SET_CUSTOM_IDENITIFIER_TEMPLATE, customIdentifierRequest,
                         EmptyFrejaResponse.class, customIdentifierRequest.getRelyingPartyId());
    }

    public void delete(DeleteCustomIdentifierRequest deleteCustomIdentifierRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        httpService.send(getUrl(serverAddress, MethodUrl.CUSTOM_IDENTIFIER_DELETE),
                         RequestTemplate.DELETE_CUSTOM_IDENTIFIER_TEMPLATE, deleteCustomIdentifierRequest,
                         EmptyFrejaResponse.class, deleteCustomIdentifierRequest.getRelyingPartyId());
    }
}
