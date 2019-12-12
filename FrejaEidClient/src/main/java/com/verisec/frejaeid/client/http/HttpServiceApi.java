package com.verisec.frejaeid.client.http;

import com.verisec.frejaeid.client.beans.common.FrejaHttpResponse;
import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.util.RequestTemplate;

public interface HttpServiceApi {

    public <Response extends FrejaHttpResponse> Response send(String methodUrl, RequestTemplate requestTemplate, RelyingPartyRequest relyingPartyRequest, Class<Response> responseType, String relyingPartyId) throws FrejaEidClientInternalException, FrejaEidException;

}
