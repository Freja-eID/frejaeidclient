package com.verisec.frejaeid.client.http;

import com.verisec.frejaeid.client.beans.custodianship.get.GetUserCustodianshipStatusRequest;
import com.verisec.frejaeid.client.beans.custodianship.get.GetUserCustodianshipStatusResponse;
import com.verisec.frejaeid.client.client.impl.CustodianshipClient;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.enums.HttpStatusCode;
import com.verisec.frejaeid.client.enums.UserCustodianshipStatusResult;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.util.JsonService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class CustodianshipClientHttpTest extends CommonHttpTest {

    private static GetUserCustodianshipStatusResponse getUserCustodianshipStatusResponse;
    private static CustodianshipClient custodianshipClient;

    @BeforeClass
    public static void init() throws FrejaEidClientInternalException {
        jsonService = new JsonService();
        custodianshipClient = CustodianshipClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setTestModeServerCustomUrl("http://localhost:" + MOCK_SERVICE_PORT).build();
        getUserCustodianshipStatusResponse =
                new GetUserCustodianshipStatusResponse(UserCustodianshipStatusResult.USER_HAS_CUSTODIAN.getStatus());

    }

    private void sendGetCustodianshipStatusRequestAndAssertResponse(GetUserCustodianshipStatusRequest validRequest)
            throws IOException, FrejaEidClientInternalException, InterruptedException, FrejaEidException {
        sendGetCustodianshipStatusRequestAndAssertResponse(validRequest, validRequest);
    }

    private void sendGetCustodianshipStatusRequestAndAssertResponse(GetUserCustodianshipStatusRequest expectedRequest,
                                                                    GetUserCustodianshipStatusRequest validRequest)
            throws FrejaEidClientInternalException, IOException, FrejaEidException, InterruptedException {
        String getCustodianshipStatusResponseString = jsonService.serializeToJson(getUserCustodianshipStatusResponse);
        startMockServer(expectedRequest, HttpStatusCode.OK.getCode(), getCustodianshipStatusResponseString);
        String result = custodianshipClient.getUserCustodianshipStatus(validRequest);
        stopServer();
        Assert.assertEquals(UserCustodianshipStatusResult.USER_HAS_CUSTODIAN.getStatus(), result);
    }

    @Test
    public void getUserCustodianshipStatus_success()
            throws IOException, FrejaEidClientInternalException, InterruptedException, FrejaEidException {

        GetUserCustodianshipStatusRequest getUserCustodianshipStatusRequest =
                GetUserCustodianshipStatusRequest.create(USER_COUNTRY_ID_AND_CRN);
        sendGetCustodianshipStatusRequestAndAssertResponse(getUserCustodianshipStatusRequest);

        GetUserCustodianshipStatusRequest getUserCustodianshipStatusRequestWithRelyingPartyId =
                GetUserCustodianshipStatusRequest.createCustom()
                        .setUserCountryIdAndCrn(USER_COUNTRY_ID_AND_CRN)
                        .setRelyingPartyId(RELYING_PARTY_ID)
                        .build();
        GetUserCustodianshipStatusRequest expectedGetUserCustodianshipStatusRequestWithRelyingPartyId =
                GetUserCustodianshipStatusRequest.createCustom().setUserCountryIdAndCrn(USER_COUNTRY_ID_AND_CRN)
                        .build();
        sendGetCustodianshipStatusRequestAndAssertResponse(expectedGetUserCustodianshipStatusRequestWithRelyingPartyId,
                                                           getUserCustodianshipStatusRequestWithRelyingPartyId);
    }
}
