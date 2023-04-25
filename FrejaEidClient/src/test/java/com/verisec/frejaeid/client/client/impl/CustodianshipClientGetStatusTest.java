package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.beans.custodianship.get.GetUserCustodianshipStatusRequest;
import com.verisec.frejaeid.client.beans.custodianship.get.GetUserCustodianshipStatusResponse;
import com.verisec.frejaeid.client.client.api.CustodianshipClientApi;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.enums.UserCustodianshipStatusResult;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.http.HttpServiceApi;
import com.verisec.frejaeid.client.util.MethodUrl;
import com.verisec.frejaeid.client.util.RequestTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CustodianshipClientGetStatusTest {
    private final HttpServiceApi httpServiceMock = Mockito.mock(HttpServiceApi.class);

    private static final String EMAIL = "test@verisec.se";
    private static final String SSN = "199207295578";
    private static final String CUSTOM_IDENTIFIER = "vealmar";
    private static final String RELYING_PARTY_ID = "verisec_integrator";
    private static final String USER_COUNTRY_ID_AND_CRN = "SE12345678";
    private final GetUserCustodianshipStatusResponse expectedResponse =
            new GetUserCustodianshipStatusResponse(UserCustodianshipStatusResult.USER_HAS_CUSTODIAN.getStatus());
    private static CustodianshipClientApi custodianshipClient;

    @Before
    public void initialiseClient() throws FrejaEidClientInternalException {
        custodianshipClient = CustodianshipClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setHttpService(httpServiceMock)
                .build();
    }

    @Test
    public void getUserCustodianshipStatus_success()
            throws FrejaEidClientInternalException, FrejaEidException {
        GetUserCustodianshipStatusRequest request = GetUserCustodianshipStatusRequest.create(USER_COUNTRY_ID_AND_CRN);
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(GetUserCustodianshipStatusRequest.class),
                                          Mockito.eq(GetUserCustodianshipStatusResponse.class),
                                          (String) Mockito.isNull())).thenReturn(expectedResponse);
        String userCustodianshipStatus = custodianshipClient.getUserCustodianshipStatus(request);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl() +
                                                     MethodUrl.CUSTODIANSHIP_GET_USER_STATUS,
                                             RequestTemplate.GET_CUSTODIANSHIP_STATUS_TEMPLATE, request,
                                             GetUserCustodianshipStatusResponse.class, null);
        Assert.assertEquals(expectedResponse.getCustodianshipStatus(), userCustodianshipStatus);
    }

    @Test
    public void getUserCustodianshipStatus_relyingPartyIdFromRequest_expectedSuccess()
            throws FrejaEidClientInternalException, FrejaEidException {
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(GetUserCustodianshipStatusRequest.class),
                                          Mockito.eq(GetUserCustodianshipStatusResponse.class), Mockito.anyString()))
                .thenReturn(expectedResponse);
        GetUserCustodianshipStatusRequest getUserCustodianshipStatusRequest =
                GetUserCustodianshipStatusRequest.createCustom().setUserCountryIdAndCrn(USER_COUNTRY_ID_AND_CRN)
                        .setRelyingPartyId(RELYING_PARTY_ID).build();
            custodianshipClient.getUserCustodianshipStatus(getUserCustodianshipStatusRequest);
            Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl() +
                                                         MethodUrl.CUSTODIANSHIP_GET_USER_STATUS,
                                                 RequestTemplate.GET_CUSTODIANSHIP_STATUS_TEMPLATE,
                                                 getUserCustodianshipStatusRequest,
                                                 GetUserCustodianshipStatusResponse.class, RELYING_PARTY_ID);
        }

}
