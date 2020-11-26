package com.verisec.frejaeid.client.http;

import com.verisec.frejaeid.client.beans.common.EmptyFrejaResponse;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.beans.usermanagement.customidentifier.delete.DeleteCustomIdentifierRequest;
import com.verisec.frejaeid.client.beans.usermanagement.customidentifier.set.SetCustomIdentifierRequest;
import com.verisec.frejaeid.client.client.impl.CustomIdentifierClient;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.Country;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.enums.HttpStatusCode;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.util.JsonService;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class CustomIdentifierClientHttpTest extends CommonHttpTest {

    private static CustomIdentifierClient customIdentifierClient;

    @BeforeClass
    public static void init() throws FrejaEidClientInternalException {
        jsonService = new JsonService();
        customIdentifierClient = CustomIdentifierClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setTestModeCustomUrl("http://localhost:" + MOCK_SERVICE_PORT).build();

    }

    private void sendSetCustomIdentifierRequestAndAssertResponse(SetCustomIdentifierRequest validRequest)
            throws IOException, FrejaEidClientInternalException, InterruptedException, FrejaEidException {
        sendSetCustomIdentifierRequestAndAssertResponse(validRequest, validRequest);
    }

    private void sendSetCustomIdentifierRequestAndAssertResponse(SetCustomIdentifierRequest expectedRequest,
                                                                 SetCustomIdentifierRequest validRequest)
            throws FrejaEidClientInternalException, IOException, FrejaEidException, InterruptedException {
        String setCustomIdResponseString = jsonService.serializeToJson(EmptyFrejaResponse.INSTANCE);
        startMockServer(expectedRequest, HttpStatusCode.NO_CONTENT.getCode(), setCustomIdResponseString);
        customIdentifierClient.set(validRequest);
        stopServer();
    }

    @Test
    public void setCustomAttribute_success()
            throws IOException, FrejaEidClientInternalException, InterruptedException, FrejaEidException {
        SetCustomIdentifierRequest customIdentifierRequestDefaultEmail =
                SetCustomIdentifierRequest.createDefaultWithEmail(EMAIL, CUSTOM_IDENTIFIER);
        sendSetCustomIdentifierRequestAndAssertResponse(customIdentifierRequestDefaultEmail);
        SetCustomIdentifierRequest customIdentifierRequestDefaultSsn =
                SetCustomIdentifierRequest.createDefaultWithSsn(SsnUserInfo.create(Country.NORWAY, SSN),
                                                                CUSTOM_IDENTIFIER);
        sendSetCustomIdentifierRequestAndAssertResponse(customIdentifierRequestDefaultSsn);
        SetCustomIdentifierRequest customIdentifierRequestEmail =
                SetCustomIdentifierRequest.createCustom().setEmailAndCustomIdentifier(EMAIL, CUSTOM_IDENTIFIER).build();
        sendSetCustomIdentifierRequestAndAssertResponse(customIdentifierRequestEmail);
        SetCustomIdentifierRequest customIdentifierRequestSsn =
                SetCustomIdentifierRequest.createCustom()
                        .setSsnAndCustomIdentifier(SsnUserInfo.create(Country.NORWAY, SSN), CUSTOM_IDENTIFIER).build();
        sendSetCustomIdentifierRequestAndAssertResponse(customIdentifierRequestSsn);
        SetCustomIdentifierRequest customIdentifierRequestPhoneNum =
                SetCustomIdentifierRequest.createCustom()
                        .setPhoneNumberAndCustomIdentifier(EMAIL, CUSTOM_IDENTIFIER).build();
        sendSetCustomIdentifierRequestAndAssertResponse(customIdentifierRequestPhoneNum);
        SetCustomIdentifierRequest customIdentifierRequestWithRelyingPartyId =
                SetCustomIdentifierRequest.createCustom()
                        .setEmailAndCustomIdentifier(EMAIL, CUSTOM_IDENTIFIER)
                        .setRelyingPartyId(RELYING_PARTY_ID).build();
        SetCustomIdentifierRequest expectedCustomIdentifierRequestWithRelyingPartyId =
                SetCustomIdentifierRequest.createCustom().setEmailAndCustomIdentifier(EMAIL, CUSTOM_IDENTIFIER).build();
        sendSetCustomIdentifierRequestAndAssertResponse(expectedCustomIdentifierRequestWithRelyingPartyId,
                                                        customIdentifierRequestWithRelyingPartyId);
    }

    @Test
    public void deleteCustomAttribute_sendRequestWithoutRelyingPartyId_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        DeleteCustomIdentifierRequest deleteRequest = DeleteCustomIdentifierRequest.create(CUSTOM_IDENTIFIER);
        String deleteCustomIdResponseString = jsonService.serializeToJson(EmptyFrejaResponse.INSTANCE);
        startMockServer(deleteRequest, HttpStatusCode.NO_CONTENT.getCode(), deleteCustomIdResponseString);

        customIdentifierClient.delete(deleteRequest);
    }

    @Test
    public void deleteCustomAttribute_sendRequestWithRelyingPartyId_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        DeleteCustomIdentifierRequest deleteRequest =
                DeleteCustomIdentifierRequest.create(CUSTOM_IDENTIFIER, RELYING_PARTY_ID);
        String deleteCustomIdResponseString = jsonService.serializeToJson(EmptyFrejaResponse.INSTANCE);
        DeleteCustomIdentifierRequest expectedDeleteRequest = DeleteCustomIdentifierRequest.create(CUSTOM_IDENTIFIER);
        startMockServer(expectedDeleteRequest, HttpStatusCode.NO_CONTENT.getCode(), deleteCustomIdResponseString);

        customIdentifierClient.delete(deleteRequest);
    }

}
