package com.verisec.frejaeid.client.http;

import com.verisec.frejaeid.client.beans.authentication.cancel.CancelAuthenticationRequest;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResult;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResultRequest;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResults;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResultsRequest;
import com.verisec.frejaeid.client.beans.authentication.init.InitiateAuthenticationRequest;
import com.verisec.frejaeid.client.beans.authentication.init.InitiateAuthenticationResponse;
import com.verisec.frejaeid.client.beans.common.EmptyFrejaResponse;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.client.api.AuthenticationClientApi;
import com.verisec.frejaeid.client.client.impl.AuthenticationClient;
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

public class AuthenticationClientHttpTest extends CommonHttpTest {

    private static InitiateAuthenticationResponse initiateAuthenticationResponse;
    private static AuthenticationResult authenticationResult;
    private static AuthenticationResult authenticationResultWithRequestedAttributes;
    private static AuthenticationResults authenticationResults;
    private static AuthenticationClientApi authenticationClient;
    private static final AttributeToReturn[] ATTRIBUTES_TO_RETURN = AttributeToReturn.values();

    @BeforeClass
    public static void init() throws FrejaEidClientInternalException {
        jsonService = new JsonService();
        initiateAuthenticationResponse = new InitiateAuthenticationResponse(REFERENCE);
        authenticationResult = new AuthenticationResult(REFERENCE, TransactionStatus.STARTED, null, null, null);
        authenticationResultWithRequestedAttributes =
                new AuthenticationResult(REFERENCE, TransactionStatus.APPROVED, DETAILS, REQUESTED_ATTRIBUTES,
                                         FREJA_COOKIE);
        authenticationResults = new AuthenticationResults(Arrays.asList(authenticationResult));
        authenticationClient = AuthenticationClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setTestModeServerCustomUrl("http://localhost:" + MOCK_SERVICE_PORT)
                .setTransactionContext(TransactionContext.PERSONAL).build();
    }

    private void sendInitAuthRequestAndAssertResponse(InitiateAuthenticationRequest validRequest,
                                                      String initAuthResponseString)
            throws IOException, FrejaEidClientInternalException, InterruptedException, FrejaEidException {
        sendInitAuthRequestAndAssertResponse(validRequest, validRequest, initAuthResponseString);
    }

    private void sendInitAuthRequestAndAssertResponse(InitiateAuthenticationRequest expectedRequest,
                                                      InitiateAuthenticationRequest validRequest,
                                                      String initAuthResponseString)
            throws IOException, FrejaEidClientInternalException, InterruptedException, FrejaEidException {
        startMockServer(expectedRequest, HttpStatusCode.OK.getCode(), initAuthResponseString);
        String reference = authenticationClient.initiate(validRequest);
        stopServer();
        Assert.assertEquals(REFERENCE, reference);
    }

    @Test
    public void initAuth_success()
            throws FrejaEidClientInternalException, IOException, InterruptedException, FrejaEidException {
        String initAuthResponseString = jsonService.serializeToJson(initiateAuthenticationResponse);

        InitiateAuthenticationRequest initiateAuthenticationRequestDefaultWithEmail =
                InitiateAuthenticationRequest.createDefaultWithEmail(EMAIL);
        sendInitAuthRequestAndAssertResponse(initiateAuthenticationRequestDefaultWithEmail, initAuthResponseString);

        InitiateAuthenticationRequest initiateAuthenticationRequestDefaultWithSsn =
                InitiateAuthenticationRequest.createDefaultWithSsn(SsnUserInfo.create(Country.NORWAY, SSN));
        sendInitAuthRequestAndAssertResponse(initiateAuthenticationRequestDefaultWithSsn, initAuthResponseString);

        InitiateAuthenticationRequest initAuthenticationRequestWithRequestedAttributesUserInfoEmail =
                InitiateAuthenticationRequest.createCustom()
                        .setEmail(EMAIL)
                        .setAttributesToReturn(ATTRIBUTES_TO_RETURN)
                        .build();
        sendInitAuthRequestAndAssertResponse(initAuthenticationRequestWithRequestedAttributesUserInfoEmail,
                                             initAuthResponseString);

        InitiateAuthenticationRequest initAuthenticationRequestWithRequestedAttributesUserInfoPhoneNum =
                InitiateAuthenticationRequest.createCustom()
                        .setPhoneNumber(EMAIL)
                        .setAttributesToReturn(ATTRIBUTES_TO_RETURN)
                        .build();
        sendInitAuthRequestAndAssertResponse(initAuthenticationRequestWithRequestedAttributesUserInfoPhoneNum,
                                             initAuthResponseString);

        InitiateAuthenticationRequest initAuthenticationRequestWithRequestedAttributesUserInfoSsn =
                InitiateAuthenticationRequest.createCustom()
                        .setSsn(SsnUserInfo.create(Country.NORWAY, SSN))
                        .setAttributesToReturn(ATTRIBUTES_TO_RETURN)
                        .build();
        sendInitAuthRequestAndAssertResponse(initAuthenticationRequestWithRequestedAttributesUserInfoSsn,
                                             initAuthResponseString);

        InitiateAuthenticationRequest initAuthenticationRequestWithRequestedAttributesUserInfoInferred =
                InitiateAuthenticationRequest.createCustom()
                        .setInferred()
                        .setAttributesToReturn(ATTRIBUTES_TO_RETURN)
                        .build();
        sendInitAuthRequestAndAssertResponse(initAuthenticationRequestWithRequestedAttributesUserInfoInferred,
                                             initAuthResponseString);

        InitiateAuthenticationRequest initAuthenticationRequestWithRegistrationStateAndRelyingPartyId =
                InitiateAuthenticationRequest.createCustom()
                        .setEmail(EMAIL)
                        .setMinRegistrationLevel(MinRegistrationLevel.EXTENDED)
                        .setRelyingPartyId(RELYING_PARTY_ID)
                        .build();
        InitiateAuthenticationRequest expectedInitAuthenticationRequestWithRegistrationStateAndRelyingPartyId =
                InitiateAuthenticationRequest.createCustom()
                        .setEmail(EMAIL)
                        .setMinRegistrationLevel(MinRegistrationLevel.EXTENDED)
                        .build();

        sendInitAuthRequestAndAssertResponse(expectedInitAuthenticationRequestWithRegistrationStateAndRelyingPartyId,
                                             initAuthenticationRequestWithRegistrationStateAndRelyingPartyId,
                                             initAuthResponseString);

    }

    @Test
    public void initAuth_organisationalTransaction_success()
            throws FrejaEidClientInternalException, IOException, InterruptedException, FrejaEidException {
        AuthenticationClient authenticationClient =
                AuthenticationClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                        .setTestModeServerCustomUrl("http://localhost:" + MOCK_SERVICE_PORT)
                        .setTransactionContext(TransactionContext.ORGANISATIONAL).build();
        InitiateAuthenticationRequest initAuthenticationRequestWithRequestedAttributesUserInfoOrganisationId =
                InitiateAuthenticationRequest.createCustom()
                        .setOrganisationId(ORGANISATION_ID)
                        .setAttributesToReturn(ATTRIBUTES_TO_RETURN)
                        .build();

        String initAuthResponseString = jsonService.serializeToJson(initiateAuthenticationResponse);
        startMockServer(initAuthenticationRequestWithRequestedAttributesUserInfoOrganisationId,
                        HttpStatusCode.OK.getCode(), initAuthResponseString);
        String reference =
                authenticationClient.initiate(initAuthenticationRequestWithRequestedAttributesUserInfoOrganisationId);
        stopServer();
        Assert.assertEquals(REFERENCE, reference);

    }

    @Test
    public void getAuthResult_sendRequestWithoutRelyingPartyId_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        AuthenticationResultRequest authenticationResultRequest = AuthenticationResultRequest.create(REFERENCE);
        String authenticationResultResponseString = jsonService.serializeToJson(authenticationResult);

        startMockServer(authenticationResultRequest, HttpStatusCode.OK.getCode(), authenticationResultResponseString);

        AuthenticationResult response = authenticationClient.getResult(authenticationResultRequest);
        Assert.assertEquals(REFERENCE, response.getAuthRef());
    }

    @Test
    public void getAuthResult_sendRequestWithRelyingPartyId_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        AuthenticationResultRequest authenticationResultRequest =
                AuthenticationResultRequest.create(REFERENCE, RELYING_PARTY_ID);
        String authenticationResultResponseString = jsonService.serializeToJson(authenticationResult);
        AuthenticationResultRequest expectedAuthenticationResultRequest = AuthenticationResultRequest.create(REFERENCE);
        startMockServer(expectedAuthenticationResultRequest, HttpStatusCode.OK.getCode(),
                        authenticationResultResponseString);

        AuthenticationResult response = authenticationClient.getResult(authenticationResultRequest);
        Assert.assertEquals(REFERENCE, response.getAuthRef());
    }

    @Test
    public void getAuthResult_sendRequestWithRelyingPartyId_withRequestedAttributes_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        AuthenticationResultRequest authenticationResultRequest =
                AuthenticationResultRequest.create(REFERENCE, RELYING_PARTY_ID);
        String authenticationResultResponseString =
                jsonService.serializeToJson(authenticationResultWithRequestedAttributes);
        AuthenticationResultRequest expectedAuthenticationResultRequest = AuthenticationResultRequest.create(REFERENCE);
        startMockServer(expectedAuthenticationResultRequest, HttpStatusCode.OK.getCode(),
                        authenticationResultResponseString);

        AuthenticationResult response = authenticationClient.getResult(authenticationResultRequest);
        Assert.assertEquals(REFERENCE, response.getAuthRef());
    }

    @Test
    public void getAuthResults_sendRequestWithoutRelyingPartyId_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        AuthenticationResultsRequest authenticationResultsRequest = AuthenticationResultsRequest.create();
        String authenticationResultsResponseString = jsonService.serializeToJson(authenticationResults);

        startMockServer(authenticationResultsRequest, HttpStatusCode.OK.getCode(), authenticationResultsResponseString);

        List<AuthenticationResult> response = authenticationClient.getResults(authenticationResultsRequest);
        Assert.assertEquals(REFERENCE, response.get(0).getAuthRef());
    }

    @Test
    public void getAuthResults_sendRequestWithRelyingPartyId_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        AuthenticationResultsRequest authenticationResultsRequest =
                AuthenticationResultsRequest.create(RELYING_PARTY_ID);
        String authenticationResultsResponseString = jsonService.serializeToJson(authenticationResults);
        AuthenticationResultsRequest expectedAuthenticationResultsRequest = AuthenticationResultsRequest.create();
        startMockServer(expectedAuthenticationResultsRequest, HttpStatusCode.OK.getCode(),
                        authenticationResultsResponseString);

        List<AuthenticationResult> response = authenticationClient.getResults(authenticationResultsRequest);
        Assert.assertEquals(REFERENCE, response.get(0).getAuthRef());
    }

    @Test
    public void cancelAuth_sendRequestWithRelyingPartyId_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        CancelAuthenticationRequest cancelRequest = CancelAuthenticationRequest.create(REFERENCE, RELYING_PARTY_ID);
        String responseString = jsonService.serializeToJson(EmptyFrejaResponse.INSTANCE);
        CancelAuthenticationRequest expectedCancelRequest = CancelAuthenticationRequest.create(REFERENCE);
        startMockServer(expectedCancelRequest, HttpStatusCode.OK.getCode(), responseString);

        authenticationClient.cancel(cancelRequest);
    }

    @Test
    public void cancelAuth_sendRequestWithoutRelyingPartyId_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        CancelAuthenticationRequest cancelRequest = CancelAuthenticationRequest.create(REFERENCE);
        String responseString = jsonService.serializeToJson(EmptyFrejaResponse.INSTANCE);

        startMockServer(cancelRequest, HttpStatusCode.OK.getCode(), responseString);

        authenticationClient.cancel(cancelRequest);
    }

}
