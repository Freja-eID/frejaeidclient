package com.verisec.frejaeid.client.http;

import com.verisec.frejaeid.client.beans.common.FrejaHttpResponse;
import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.util.RequestTemplate;
import org.apache.http.HttpResponse;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface HttpServiceApi {

    public <Response extends FrejaHttpResponse> Response send(String methodUrl, RequestTemplate requestTemplate,
                                                              RelyingPartyRequest relyingPartyRequest,
                                                              Class<Response> responseType, String relyingPartyId)
            throws FrejaEidClientInternalException, FrejaEidException;

    public HttpResponse httpGet(String methodUrl, Map<String, String> parameters)
            throws FrejaEidClientInternalException, FrejaEidException, UnsupportedEncodingException;


}
