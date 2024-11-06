package com.verisec.frejaeid.client.http;

import com.verisec.frejaeid.client.beans.common.EmptyFrejaResponse;
import com.verisec.frejaeid.client.beans.general.*;
import com.verisec.frejaeid.client.beans.organisationid.cancel.CancelAddOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.delete.DeleteOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.get.OrganisationIdResult;
import com.verisec.frejaeid.client.beans.organisationid.get.OrganisationIdResultRequest;
import com.verisec.frejaeid.client.beans.organisationid.getall.GetAllOrganisationIdUsersRequest;
import com.verisec.frejaeid.client.beans.organisationid.getall.GetAllOrganisationIdUsersResponse;
import com.verisec.frejaeid.client.beans.organisationid.init.InitiateAddOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.init.InitiateAddOrganisationIdResponse;
import com.verisec.frejaeid.client.beans.organisationid.update.UpdateOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.update.UpdateOrganisationIdResponse;
import com.verisec.frejaeid.client.client.api.OrganisationIdClientApi;
import com.verisec.frejaeid.client.client.impl.OrganisationIdClient;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.*;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.util.JsonService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class OrganisationIdClientHttpTest extends CommonHttpTest {

    private static InitiateAddOrganisationIdResponse initiateAddOrganisationIdResponse;
    private static OrganisationIdResult organisationIdResult;
    private static GetAllOrganisationIdUsersResponse getAllOrganisationIdUsersResponse;
    private static OrganisationIdClientApi organisationIdClient;
    private static final String ORGANISATION_ID_TITLE = "OrganisationId title";
    private static final String IDENTIFIER_NAME = "Identifier name";
    private static final String IDENTIFIER = "identifier";
    private static final List<OrganisationIdAttribute> ADDITIONAL_ATTRIBUTES =
            Arrays.asList(OrganisationIdAttribute.create("key", "friendly name", "value"),
                          OrganisationIdAttribute.create("attribute_id", "attribute name", "attribute value"));

    @BeforeClass
    public static void init() throws FrejaEidClientInternalException {
        jsonService = new JsonService();
        initiateAddOrganisationIdResponse = new InitiateAddOrganisationIdResponse(REFERENCE);
        organisationIdResult = new OrganisationIdResult(REFERENCE, TransactionStatus.STARTED, null, null);
        OrganisationIdUserInfo organisationIdUserInfo =
                new OrganisationIdUserInfo(OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER),
                                           SSN_USER_INFO, RegistrationState.EXTENDED);
        getAllOrganisationIdUsersResponse =
                new GetAllOrganisationIdUsersResponse(Arrays.asList(organisationIdUserInfo));

        organisationIdClient = OrganisationIdClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setTestModeServerCustomUrl("http://localhost:" + MOCK_SERVICE_PORT).build();

    }

    private void sendInitiateAddOrganisationIdRequestAndAssertResponse(InitiateAddOrganisationIdRequest validRequest)
            throws IOException, FrejaEidClientInternalException, InterruptedException, FrejaEidException {
        sendInitiateAddOrganisationIdRequestAndAssertResponse(validRequest, validRequest);
    }

    private void sendInitiateAddOrganisationIdRequestAndAssertResponse(InitiateAddOrganisationIdRequest expectedRequest,
                                                                       InitiateAddOrganisationIdRequest validRequest)
            throws FrejaEidClientInternalException, IOException, FrejaEidException, InterruptedException {
        String initAddOrganisationIdResponseString = jsonService.serializeToJson(initiateAddOrganisationIdResponse);
        startMockServer(expectedRequest, HttpStatusCode.OK.getCode(), initAddOrganisationIdResponseString);
        String reference = organisationIdClient.initiateAdd(validRequest);
        stopServer();
        Assert.assertEquals(REFERENCE, reference);
    }

    @Test
    public void initAddOrganisationId_success()
            throws FrejaEidClientInternalException, IOException, InterruptedException, FrejaEidException {
        InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequestDefaultEmail =
                InitiateAddOrganisationIdRequest.createDefaultWithEmail(
                        EMAIL, OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER));
        sendInitiateAddOrganisationIdRequestAndAssertResponse(initiateAddOrganisationIdRequestDefaultEmail);

        InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequestDefaultSsn =
                InitiateAddOrganisationIdRequest.createDefaultWithSsn(
                        SsnUserInfo.create(Country.FINLAND, SSN),
                        OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER));
        sendInitiateAddOrganisationIdRequestAndAssertResponse(initiateAddOrganisationIdRequestDefaultSsn);

        InitiateAddOrganisationIdRequest initAddOrganisationIdRequestCustomEmail =
                InitiateAddOrganisationIdRequest.createCustom()
                        .setEmailAndOrganisationId(EMAIL, OrganisationId.create(ORGANISATION_ID_TITLE,
                                                                                IDENTIFIER_NAME, IDENTIFIER))
                        .setExpiry(Long.MAX_VALUE)
                        .setMinRegistrationLevel(MinRegistrationLevel.PLUS)
                        .build();
        sendInitiateAddOrganisationIdRequestAndAssertResponse(initAddOrganisationIdRequestCustomEmail);

        InitiateAddOrganisationIdRequest initAddOrganisationIdRequestCustomSsn =
                InitiateAddOrganisationIdRequest.createCustom()
                        .setSsnAndOrganisationId(SsnUserInfo.create(Country.DENMARK, SSN),
                                                 OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME,
                                                                       IDENTIFIER))
                        .setExpiry(Long.MAX_VALUE)
                        .setMinRegistrationLevel(MinRegistrationLevel.EXTENDED)
                        .build();

        sendInitiateAddOrganisationIdRequestAndAssertResponse(initAddOrganisationIdRequestCustomSsn);

        InitiateAddOrganisationIdRequest initAddOrganisationIdRequestCustomPhoneNum =
                InitiateAddOrganisationIdRequest.createCustom()
                        .setPhoneNumberAndOrganisationId(EMAIL, OrganisationId.create(ORGANISATION_ID_TITLE,
                                                                                      IDENTIFIER_NAME, IDENTIFIER))
                        .setExpiry(Long.MAX_VALUE)
                        .setMinRegistrationLevel(MinRegistrationLevel.BASIC)
                        .build();
        sendInitiateAddOrganisationIdRequestAndAssertResponse(initAddOrganisationIdRequestCustomPhoneNum);

        InitiateAddOrganisationIdRequest initAddOrganisationIdRequestWithRelyingPartyId =
                InitiateAddOrganisationIdRequest.createCustom()
                        .setEmailAndOrganisationId(EMAIL, OrganisationId.create(ORGANISATION_ID_TITLE,
                                                                                IDENTIFIER_NAME, IDENTIFIER))
                        .setExpiry(Long.MAX_VALUE)
                        .setMinRegistrationLevel(MinRegistrationLevel.PLUS)
                        .setRelyingPartyId(RELYING_PARTY_ID)
                        .build();

        InitiateAddOrganisationIdRequest expectedInitAddOrganisationIdRequestWithRelyingPartyId =
                InitiateAddOrganisationIdRequest.createCustom()
                        .setEmailAndOrganisationId(EMAIL, OrganisationId.create(ORGANISATION_ID_TITLE,
                                                                                IDENTIFIER_NAME, IDENTIFIER))
                        .setExpiry(Long.MAX_VALUE)
                        .setMinRegistrationLevel(MinRegistrationLevel.PLUS)
                        .build();
        sendInitiateAddOrganisationIdRequestAndAssertResponse(expectedInitAddOrganisationIdRequestWithRelyingPartyId,
                                                              initAddOrganisationIdRequestWithRelyingPartyId);

        InitiateAddOrganisationIdRequest initAddOrganisationIdRequestWithDefaultValues =
                InitiateAddOrganisationIdRequest.createCustom()
                        .setSsnAndOrganisationId(SsnUserInfo.create(Country.NORWAY, SSN),
                                                 OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME,
                                                                       IDENTIFIER))
                        .setRelyingPartyId(RELYING_PARTY_ID)
                        .build();
        InitiateAddOrganisationIdRequest expectedInitAddOrganisationIdRequestWithDefaultValues =
                InitiateAddOrganisationIdRequest.createCustom()
                        .setSsnAndOrganisationId(SsnUserInfo.create(Country.NORWAY, SSN),
                                                 OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME,
                                                                       IDENTIFIER))
                        .build();
        sendInitiateAddOrganisationIdRequestAndAssertResponse(expectedInitAddOrganisationIdRequestWithDefaultValues,
                                                              initAddOrganisationIdRequestWithDefaultValues);
    }

    @Test
    public void getOrganisationIdResult_sendRequestWithRelyingPartyId_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        OrganisationIdResultRequest getOneOrganisationIdResultRequest =
                OrganisationIdResultRequest.create(REFERENCE, RELYING_PARTY_ID);
        String organisationIdResultString = jsonService.serializeToJson(organisationIdResult);
        OrganisationIdResultRequest expectedGetOneOrganisationIdResultRequest =
                OrganisationIdResultRequest.create(REFERENCE);
        startMockServer(expectedGetOneOrganisationIdResultRequest, HttpStatusCode.OK.getCode(),
                        organisationIdResultString);

        OrganisationIdResult response = organisationIdClient.getResult(getOneOrganisationIdResultRequest);
        Assert.assertEquals(REFERENCE, response.getOrgIdRef());
    }

    @Test
    public void getOrganisationIdResult_sendRequestWithoutRelyingPartyId_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        OrganisationIdResultRequest getOneOrganisationIdResultRequest = OrganisationIdResultRequest.create(REFERENCE);
        String organisationIdResultString = jsonService.serializeToJson(organisationIdResult);

        startMockServer(getOneOrganisationIdResultRequest, HttpStatusCode.OK.getCode(), organisationIdResultString);

        OrganisationIdResult response = organisationIdClient.getResult(getOneOrganisationIdResultRequest);
        Assert.assertEquals(REFERENCE, response.getOrgIdRef());
    }

    @Test
    public void cancelAddOrganisationId_sendRequestWithoutRelyingPartyId_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        CancelAddOrganisationIdRequest cancelRequest = CancelAddOrganisationIdRequest.create(REFERENCE);
        String responseString = jsonService.serializeToJson(EmptyFrejaResponse.INSTANCE);

        startMockServer(cancelRequest, HttpStatusCode.OK.getCode(), responseString);

        organisationIdClient.cancelAdd(cancelRequest);
    }

    @Test
    public void cancelAddOrganisationId_sendRequestWithRelyingPartyId_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        CancelAddOrganisationIdRequest cancelRequest =
                CancelAddOrganisationIdRequest.create(REFERENCE, RELYING_PARTY_ID);
        String responseString = jsonService.serializeToJson(EmptyFrejaResponse.INSTANCE);
        CancelAddOrganisationIdRequest expectedCancelRequest = CancelAddOrganisationIdRequest.create(REFERENCE);
        startMockServer(expectedCancelRequest, HttpStatusCode.OK.getCode(), responseString);

        organisationIdClient.cancelAdd(cancelRequest);
    }

    @Test
    public void deleteOrganisationId_sendRequestWithoutRelyingPartyId_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        DeleteOrganisationIdRequest deleteOrganisationIdRequest = DeleteOrganisationIdRequest.create(IDENTIFIER);
        String responseString = jsonService.serializeToJson(EmptyFrejaResponse.INSTANCE);

        startMockServer(deleteOrganisationIdRequest, HttpStatusCode.OK.getCode(), responseString);

        organisationIdClient.delete(deleteOrganisationIdRequest);
    }

    @Test
    public void deleteOrganisationId_sendRequestWithRelyingPartyId_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        DeleteOrganisationIdRequest deleteOrganisationIdRequest =
                DeleteOrganisationIdRequest.create(IDENTIFIER, RELYING_PARTY_ID);
        String responseString = jsonService.serializeToJson(EmptyFrejaResponse.INSTANCE);
        DeleteOrganisationIdRequest expectedDeleteOrganisationIdRequest =
                DeleteOrganisationIdRequest.create(IDENTIFIER);
        startMockServer(expectedDeleteOrganisationIdRequest, HttpStatusCode.OK.getCode(), responseString);

        organisationIdClient.delete(deleteOrganisationIdRequest);
    }

    @Test
    public void getAllOrganisationIdUsers_sendRequestWithoutRelyingPartyId_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        GetAllOrganisationIdUsersRequest getAllOrganisationIdUsersRequest = GetAllOrganisationIdUsersRequest.create();
        String responseString = jsonService.serializeToJson(getAllOrganisationIdUsersResponse);

        startMockServer(getAllOrganisationIdUsersRequest, HttpStatusCode.OK.getCode(), responseString);

        List<OrganisationIdUserInfo> actualListOfOrganisationIdUserInfos =
                organisationIdClient.getAllUsers(getAllOrganisationIdUsersRequest);
        Assert.assertEquals(getAllOrganisationIdUsersResponse.getUserInfos(), actualListOfOrganisationIdUserInfos);
    }

    @Test
    public void getAllOrganisationIdUsers_sendRequestWithRelyingPartyId_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        GetAllOrganisationIdUsersRequest getAllOrganisationIdUsersRequest =
                GetAllOrganisationIdUsersRequest.create(RELYING_PARTY_ID);
        String responseString = jsonService.serializeToJson(getAllOrganisationIdUsersResponse);
        startMockServer(getAllOrganisationIdUsersRequest, HttpStatusCode.OK.getCode(), responseString);

        List<OrganisationIdUserInfo> actualListOfOrganisationIdUserInfos =
                organisationIdClient.getAllUsers(getAllOrganisationIdUsersRequest);
        Assert.assertEquals(getAllOrganisationIdUsersResponse.getUserInfos(), actualListOfOrganisationIdUserInfos);
    }

    @Test
    public void updateOrganisationId_sendRequestWithoutRelyingPartyId_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        UpdateOrganisationIdRequest updateOrganisationIdRequest =
                UpdateOrganisationIdRequest.create(IDENTIFIER, ADDITIONAL_ATTRIBUTES);
        UpdateOrganisationIdResponse expectedResponse = new UpdateOrganisationIdResponse(new UpdateOrganisationIdStatus(1, 1, 0));
        String responseString = jsonService.serializeToJson(expectedResponse);
        startMockServer(updateOrganisationIdRequest, HttpStatusCode.OK.getCode(), responseString);
        UpdateOrganisationIdResponse receivedResponse = organisationIdClient.update(updateOrganisationIdRequest);
        Assert.assertEquals(expectedResponse, receivedResponse);
    }

    @Test
    public void updateOrganisationId_sendRequestWithRelyingPartyId_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        UpdateOrganisationIdRequest updateOrganisationIdRequest =
                UpdateOrganisationIdRequest.create(IDENTIFIER, ADDITIONAL_ATTRIBUTES, RELYING_PARTY_ID);
        UpdateOrganisationIdResponse expectedResponse = new UpdateOrganisationIdResponse(new UpdateOrganisationIdStatus(0, 2, 0));
        String responseString = jsonService.serializeToJson(expectedResponse);
        startMockServer(updateOrganisationIdRequest, HttpStatusCode.OK.getCode(), responseString);
        UpdateOrganisationIdResponse receivedResponse = organisationIdClient.update(updateOrganisationIdRequest);
        Assert.assertEquals(expectedResponse, receivedResponse);
    }
}
