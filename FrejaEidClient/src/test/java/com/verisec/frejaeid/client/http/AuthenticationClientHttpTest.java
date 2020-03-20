package com.verisec.frejaeid.client.http;

import com.verisec.frejaeid.client.beans.common.EmptyFrejaResponse;
import com.verisec.frejaeid.client.beans.authentication.cancel.CancelAuthenticationRequest;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResultsRequest;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResults;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResultRequest;
import com.verisec.frejaeid.client.beans.authentication.get.AuthenticationResult;
import com.verisec.frejaeid.client.beans.authentication.init.InitiateAuthenticationRequest;
import com.verisec.frejaeid.client.beans.authentication.init.InitiateAuthenticationResponse;
import com.verisec.frejaeid.client.beans.general.SslSettings;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.client.api.AuthenticationClientApi;
import com.verisec.frejaeid.client.client.impl.AuthenticationClient;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.TransactionStatus;
import com.verisec.frejaeid.client.enums.AttributeToReturn;
import com.verisec.frejaeid.client.enums.Country;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.enums.HttpStatusCode;
import com.verisec.frejaeid.client.enums.MinRegistrationLevel;
import com.verisec.frejaeid.client.enums.TransactionContext;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.util.JsonService;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class AuthenticationClientHttpTest extends CommonHttpTest {

    private static InitiateAuthenticationResponse initiateAuthenticationResponse;
    private static AuthenticationResult authenticationResult;
    private static AuthenticationResult authenticationResultWithRequestedAttributes;
    private static AuthenticationResults authenticationResults;
    private static AuthenticationClientApi authenticationClient;

    @BeforeClass
    public static void init() throws FrejaEidClientInternalException {
        jsonService = new JsonService();
        initiateAuthenticationResponse = new InitiateAuthenticationResponse(REFERENCE);
        authenticationResult = new AuthenticationResult(REFERENCE, TransactionStatus.STARTED, null, null);
        authenticationResultWithRequestedAttributes = new AuthenticationResult(REFERENCE, TransactionStatus.APPROVED, DETAILS, REQUESTED_ATTRIBUTES);
        authenticationResults = new AuthenticationResults(Arrays.asList(authenticationResult));
        authenticationClient = AuthenticationClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                .setTestModeCustomUrl("http://localhost:" + MOCK_SERVICE_PORT).setTransactionContext(TransactionContext.PERSONAL).build();
    }

    private void sendInitAuthRequestAndAssertResponse(InitiateAuthenticationRequest validRequest, String initAuthResponseString) throws IOException, FrejaEidClientInternalException, InterruptedException, FrejaEidException {
        sendInitAuthRequestAndAssertResponse(validRequest, validRequest, initAuthResponseString);
    }

    private void sendInitAuthRequestAndAssertResponse(InitiateAuthenticationRequest expectedRequest, InitiateAuthenticationRequest validRequest, String initAuthResponseString) throws IOException, FrejaEidClientInternalException, InterruptedException, FrejaEidException {
        startMockServer(expectedRequest, HttpStatusCode.OK.getCode(), initAuthResponseString);
        String reference = authenticationClient.initiate(validRequest);
        stopServer();
        Assert.assertEquals(REFERENCE, reference);
    }

    @Test
    public void initAuth_success() throws FrejaEidClientInternalException, IOException, InterruptedException, FrejaEidException {
        String initAuthResponseString = jsonService.serializeToJson(initiateAuthenticationResponse);

        InitiateAuthenticationRequest initiateAuthenticationRequestDefaultWithEmail = InitiateAuthenticationRequest.createDefaultWithEmail(EMAIL);
        sendInitAuthRequestAndAssertResponse(initiateAuthenticationRequestDefaultWithEmail, initAuthResponseString);

        InitiateAuthenticationRequest initiateAuthenticationRequestDefaultWithSsn = InitiateAuthenticationRequest.createDefaultWithSsn(new SsnUserInfo(Country.NORWAY, SSN));
        sendInitAuthRequestAndAssertResponse(initiateAuthenticationRequestDefaultWithSsn, initAuthResponseString);

        InitiateAuthenticationRequest initAuthenticationRequestWithRequestedAttributesUserInfoEmail = InitiateAuthenticationRequest.createCustom()
                .setEmail(EMAIL)
                .setAttributesToReturn(AttributeToReturn.BASIC_USER_INFO, AttributeToReturn.EMAIL_ADDRESS, AttributeToReturn.SSN, AttributeToReturn.CUSTOM_IDENTIFIER, AttributeToReturn.INTEGRATOR_SPECIFIC_USER_ID, AttributeToReturn.ADDRESSES)
                .build();
        sendInitAuthRequestAndAssertResponse(initAuthenticationRequestWithRequestedAttributesUserInfoEmail, initAuthResponseString);

        InitiateAuthenticationRequest initAuthenticationRequestWithRequestedAttributesUserInfoPhoneNum = InitiateAuthenticationRequest.createCustom()
                .setPhoneNumber(EMAIL)
                .setAttributesToReturn(AttributeToReturn.BASIC_USER_INFO, AttributeToReturn.EMAIL_ADDRESS, AttributeToReturn.SSN, AttributeToReturn.CUSTOM_IDENTIFIER, AttributeToReturn.INTEGRATOR_SPECIFIC_USER_ID, AttributeToReturn.ADDRESSES)
                .build();
        sendInitAuthRequestAndAssertResponse(initAuthenticationRequestWithRequestedAttributesUserInfoPhoneNum, initAuthResponseString);

        InitiateAuthenticationRequest initAuthenticationRequestWithRequestedAttributesUserInfoSsn = InitiateAuthenticationRequest.createCustom()
                .setSsn(new SsnUserInfo(Country.NORWAY, SSN))
                .setAttributesToReturn(AttributeToReturn.BASIC_USER_INFO, AttributeToReturn.EMAIL_ADDRESS, AttributeToReturn.SSN, AttributeToReturn.CUSTOM_IDENTIFIER, AttributeToReturn.INTEGRATOR_SPECIFIC_USER_ID, AttributeToReturn.ADDRESSES)
                .build();
        sendInitAuthRequestAndAssertResponse(initAuthenticationRequestWithRequestedAttributesUserInfoSsn, initAuthResponseString);

        InitiateAuthenticationRequest initAuthenticationRequestWithRequestedAttributesUserInfoInferred = InitiateAuthenticationRequest.createCustom()
                .setInferred()
                .setAttributesToReturn(AttributeToReturn.BASIC_USER_INFO, AttributeToReturn.EMAIL_ADDRESS, AttributeToReturn.SSN, AttributeToReturn.CUSTOM_IDENTIFIER, AttributeToReturn.INTEGRATOR_SPECIFIC_USER_ID, AttributeToReturn.ADDRESSES)
                .build();
        sendInitAuthRequestAndAssertResponse(initAuthenticationRequestWithRequestedAttributesUserInfoInferred, initAuthResponseString);

        InitiateAuthenticationRequest initAuthenticationRequestWithRegistrationStateAndRelyingPartyId = InitiateAuthenticationRequest.createCustom()
                .setEmail(EMAIL)
                .setMinRegistrationLevel(MinRegistrationLevel.EXTENDED)
                .setRelyingPartyId(RELYING_PARTY_ID)
                .build();
        InitiateAuthenticationRequest expectedInitAuthenticationRequestWithRegistrationStateAndRelyingPartyId = InitiateAuthenticationRequest.createCustom()
                .setEmail(EMAIL)
                .setMinRegistrationLevel(MinRegistrationLevel.EXTENDED)
                .build();

        sendInitAuthRequestAndAssertResponse(expectedInitAuthenticationRequestWithRegistrationStateAndRelyingPartyId, initAuthenticationRequestWithRegistrationStateAndRelyingPartyId, initAuthResponseString);

    }

    @Test
    public void initAuth_organisationalTransaction_success() throws FrejaEidClientInternalException, IOException, InterruptedException, FrejaEidException {
        AuthenticationClient authenticationClient = AuthenticationClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                .setTestModeCustomUrl("http://localhost:" + MOCK_SERVICE_PORT).setTransactionContext(TransactionContext.ORGANISATIONAL).build();
        InitiateAuthenticationRequest initAuthenticationRequestWithRequestedAttributesUserInfoOrganisationId = InitiateAuthenticationRequest.createCustom()
                .setOrganisationId(ORGANISATION_ID)
                .setAttributesToReturn(AttributeToReturn.BASIC_USER_INFO, AttributeToReturn.EMAIL_ADDRESS, AttributeToReturn.SSN, AttributeToReturn.CUSTOM_IDENTIFIER, AttributeToReturn.INTEGRATOR_SPECIFIC_USER_ID, AttributeToReturn.ORGANISATION_ID_IDENTIFIER, AttributeToReturn.ADDRESSES)
                .build();

        String initAuthResponseString = jsonService.serializeToJson(initiateAuthenticationResponse);
        startMockServer(initAuthenticationRequestWithRequestedAttributesUserInfoOrganisationId, HttpStatusCode.OK.getCode(), initAuthResponseString);
        String reference = authenticationClient.initiate(initAuthenticationRequestWithRequestedAttributesUserInfoOrganisationId);
        stopServer();
        Assert.assertEquals(REFERENCE, reference);

    }

    @Test
    public void getAuthResult_sendRequestWithoutRelyingPartyId_success() throws FrejaEidClientInternalException, IOException, FrejaEidException {
        AuthenticationResultRequest authenticationResultRequest = AuthenticationResultRequest.create(REFERENCE);
        String authenticationResultResponseString = jsonService.serializeToJson(authenticationResult);

        startMockServer(authenticationResultRequest, HttpStatusCode.OK.getCode(), authenticationResultResponseString);

        AuthenticationResult response = authenticationClient.getResult(authenticationResultRequest);
        Assert.assertEquals(REFERENCE, response.getAuthRef());
    }

    @Test
    public void getAuthResult_sendRequestWithRelyingPartyId_success() throws FrejaEidClientInternalException, IOException, FrejaEidException {
        AuthenticationResultRequest authenticationResultRequest = AuthenticationResultRequest.create(REFERENCE, RELYING_PARTY_ID);
        String authenticationResultResponseString = jsonService.serializeToJson(authenticationResult);
        AuthenticationResultRequest expectedAuthenticationResultRequest = AuthenticationResultRequest.create(REFERENCE);
        startMockServer(expectedAuthenticationResultRequest, HttpStatusCode.OK.getCode(), authenticationResultResponseString);

        AuthenticationResult response = authenticationClient.getResult(authenticationResultRequest);
        Assert.assertEquals(REFERENCE, response.getAuthRef());
    }

    @Test
    public void getAuthResult_sendRequestWithRelyingPartyId_withRequestedAttributes_success() throws FrejaEidClientInternalException, IOException, FrejaEidException {
        AuthenticationResultRequest authenticationResultRequest = AuthenticationResultRequest.create(REFERENCE, RELYING_PARTY_ID);
        String authenticationResultResponseString = jsonService.serializeToJson(authenticationResultWithRequestedAttributes);
        AuthenticationResultRequest expectedAuthenticationResultRequest = AuthenticationResultRequest.create(REFERENCE);
        startMockServer(expectedAuthenticationResultRequest, HttpStatusCode.OK.getCode(), authenticationResultResponseString);

        AuthenticationResult response = authenticationClient.getResult(authenticationResultRequest);
        Assert.assertEquals(REFERENCE, response.getAuthRef());
    }

    @Test
    public void getAuthResults_sendRequestWithoutRelyingPartyId_success() throws FrejaEidClientInternalException, IOException, FrejaEidException {
        AuthenticationResultsRequest authenticationResultsRequest = AuthenticationResultsRequest.create();
        String authenticationResultsResponseString = jsonService.serializeToJson(authenticationResults);

        startMockServer(authenticationResultsRequest, HttpStatusCode.OK.getCode(), authenticationResultsResponseString);

        List<AuthenticationResult> response = authenticationClient.getResults(authenticationResultsRequest);
        Assert.assertEquals(REFERENCE, response.get(0).getAuthRef());
    }

    @Test
    public void getAuthResults_sendRequestWithRelyingPartyId_success() throws FrejaEidClientInternalException, IOException, FrejaEidException {
        AuthenticationResultsRequest authenticationResultsRequest = AuthenticationResultsRequest.create(RELYING_PARTY_ID);
        String authenticationResultsResponseString = jsonService.serializeToJson(authenticationResults);
        AuthenticationResultsRequest expectedAuthenticationResultsRequest = AuthenticationResultsRequest.create();
        startMockServer(expectedAuthenticationResultsRequest, HttpStatusCode.OK.getCode(), authenticationResultsResponseString);

        List<AuthenticationResult> response = authenticationClient.getResults(authenticationResultsRequest);
        Assert.assertEquals(REFERENCE, response.get(0).getAuthRef());
    }

    @Test
    public void cancelAuth_sendRequestWithRelyingPartyId_success() throws FrejaEidClientInternalException, IOException, FrejaEidException {
        CancelAuthenticationRequest cancelRequest = CancelAuthenticationRequest.create(REFERENCE, RELYING_PARTY_ID);
        String responseString = jsonService.serializeToJson(EmptyFrejaResponse.INSTANCE);
        CancelAuthenticationRequest expectedCancelRequest = CancelAuthenticationRequest.create(REFERENCE);
        startMockServer(expectedCancelRequest, HttpStatusCode.OK.getCode(), responseString);

        authenticationClient.cancel(cancelRequest);
    }

    @Test
    public void cancelAuth_sendRequestWithoutRelyingPartyId_success() throws FrejaEidClientInternalException, IOException, FrejaEidException {
        CancelAuthenticationRequest cancelRequest = CancelAuthenticationRequest.create(REFERENCE);
        String responseString = jsonService.serializeToJson(EmptyFrejaResponse.INSTANCE);

        startMockServer(cancelRequest, HttpStatusCode.OK.getCode(), responseString);

        authenticationClient.cancel(cancelRequest);
    }

}
