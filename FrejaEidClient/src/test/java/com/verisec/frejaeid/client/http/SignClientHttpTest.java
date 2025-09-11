package com.verisec.frejaeid.client.http;

import com.verisec.frejaeid.client.beans.common.EmptyFrejaResponse;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.beans.sign.cancel.CancelSignRequest;
import com.verisec.frejaeid.client.beans.sign.get.SignResult;
import com.verisec.frejaeid.client.beans.sign.get.SignResultRequest;
import com.verisec.frejaeid.client.beans.sign.get.SignResults;
import com.verisec.frejaeid.client.beans.sign.get.SignResultsRequest;
import com.verisec.frejaeid.client.beans.sign.init.DataToSign;
import com.verisec.frejaeid.client.beans.sign.init.InitiateSignRequest;
import com.verisec.frejaeid.client.beans.sign.init.InitiateSignResponse;
import com.verisec.frejaeid.client.beans.sign.init.PushNotification;
import com.verisec.frejaeid.client.client.api.SignClientApi;
import com.verisec.frejaeid.client.client.impl.SignClient;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.*;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.util.JsonService;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class SignClientHttpTest extends CommonHttpTest {

    private static String title;
    private static PushNotification pushNotification;
    private static String pushNotificationTitle;
    private static String pushNotificationText;
    private static String dataToSignText;
    private static byte[] binaryData;
    private static InitiateSignResponse initiateSignResponse;
    private static SignResult signResult;
    private static SignResult signResultWithRequestedAttributes;
    private static SignResults signResults;
    private static SignClientApi signClient;
    private static final AttributeToReturn[] ATTRIBUTES_TO_RETURN = AttributeToReturn.values();

    @BeforeClass
    public static void init() throws FrejaEidClientInternalException {
        jsonService = new JsonService();
        title = "Sign transaction";
        pushNotificationTitle = "Sign notification title";
        pushNotificationText = "Sign notification text";
        pushNotification = PushNotification.create(pushNotificationTitle, pushNotificationText);
        dataToSignText = "Data to sign";
        binaryData = "binaryData".getBytes(StandardCharsets.UTF_8);
        initiateSignResponse = new InitiateSignResponse(REFERENCE, QR_CODE_SECRET);
        signResult = new SignResult(REFERENCE, TransactionStatus.STARTED, null, null, null);
        signResultWithRequestedAttributes = new SignResult(REFERENCE, TransactionStatus.APPROVED, DETAILS,
                                                           REQUESTED_ATTRIBUTES, FREJA_COOKIE);
        signResults = new SignResults(Arrays.asList(signResult));

        signClient = SignClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                .setTestModeServerCustomUrl("http://localhost:" + MOCK_SERVICE_PORT)
                .setTransactionContext(TransactionContext.PERSONAL).build();

    }

    private void sendInitiateV11SignRequestAndAssertResponse(InitiateSignRequest validRequest)
            throws IOException, FrejaEidClientInternalException, InterruptedException, FrejaEidException {
        sendInitiateV11SignRequestAndAssertResponse(validRequest, validRequest);
    }

    private void sendInitiateV11SignRequestAndAssertResponse(InitiateSignRequest expectedRequest,
                                                             InitiateSignRequest validRequest)
            throws FrejaEidClientInternalException, IOException, FrejaEidException, InterruptedException {
        String initSignResponseString = jsonService.serializeToJson(initiateSignResponse);
        startMockServer(expectedRequest, HttpStatusCode.OK.getCode(), initSignResponseString);
        InitiateSignResponse response = signClient.initiateV1_1(validRequest);
        stopServer();
        Assert.assertEquals(initiateSignResponse, response);
    }

    @Test
    public void initiateSign_success()
            throws FrejaEidClientInternalException, IOException, InterruptedException, FrejaEidException {
        DataToSign dataToSign =
                DataToSign.create(Base64.encodeBase64String(dataToSignText.getBytes(StandardCharsets.UTF_8)));
        InitiateSignRequest initiateSignRequestDefaultEmail =
                InitiateSignRequest.createDefaultWithEmail(EMAIL, title, dataToSignText);
        sendInitiateV11SignRequestAndAssertResponse(initiateSignRequestDefaultEmail);
        InitiateSignRequest initiateSignRequestDefaultSsn =
                InitiateSignRequest.createDefaultWithSsn(SsnUserInfo.create(
                        Country.FINLAND, SSN), title, dataToSignText);
        sendInitiateV11SignRequestAndAssertResponse(initiateSignRequestDefaultSsn);
        InitiateSignRequest initSignCustomRequestWithRequestedAttributes = InitiateSignRequest.createCustom()
                .setEmail(EMAIL)
                .setDataToSign(dataToSign)
                .setExpiry(Long.MAX_VALUE)
                .setMinRegistrationLevel(MinRegistrationLevel.BASIC)
                .setAttributesToReturn(ATTRIBUTES_TO_RETURN)
                .setPushNotification(pushNotification)
                .setTitle(title)
                .build();
        sendInitiateV11SignRequestAndAssertResponse(initSignCustomRequestWithRequestedAttributes);

        InitiateSignRequest initSignCustomRequestWithRequestedAttributesExtendedDataToSign =
                InitiateSignRequest.createCustom()
                        .setEmail(EMAIL)
                        .setDataToSign(DataToSign.create(dataToSignText, binaryData))
                        .setExpiry(Long.MAX_VALUE)
                        .setMinRegistrationLevel(MinRegistrationLevel.BASIC)
                        .setAttributesToReturn(ATTRIBUTES_TO_RETURN)
                        .setPushNotification(pushNotification)
                        .setTitle(title)
                        .build();
        sendInitiateV11SignRequestAndAssertResponse(initSignCustomRequestWithRequestedAttributesExtendedDataToSign);

        InitiateSignRequest initSignCustomRequestWithDefaultValues = InitiateSignRequest.createCustom()
                .setEmail(EMAIL)
                .setDataToSign(DataToSign.create(dataToSignText, binaryData))
                .build();
        sendInitiateV11SignRequestAndAssertResponse(initSignCustomRequestWithDefaultValues);

        InitiateSignRequest initSignCustomRequestWithRelyingPartyId = InitiateSignRequest.createCustom()
                .setEmail(EMAIL)
                .setDataToSign(DataToSign.create(dataToSignText, binaryData))
                .setRelyingPartyId(RELYING_PARTY_ID)
                .build();
        InitiateSignRequest expectedInitSignCustomRequestWithRelyingPartyId = InitiateSignRequest.createCustom()
                .setEmail(EMAIL)
                .setDataToSign(DataToSign.create(dataToSignText, binaryData))
                .build();
        sendInitiateV11SignRequestAndAssertResponse(expectedInitSignCustomRequestWithRelyingPartyId,
                                                    initSignCustomRequestWithRelyingPartyId);

        InitiateSignRequest initSignCustomRequestWithAdvancedSignatureType = InitiateSignRequest.createCustom()
                .setEmail(EMAIL)
                .setDataToSign(DataToSign.create(dataToSignText), SignatureType.XML_MINAMEDDELANDEN)
                .setMinRegistrationLevel(MinRegistrationLevel.PLUS)
                .setAttributesToReturn(ATTRIBUTES_TO_RETURN)
                .build();
        sendInitiateV11SignRequestAndAssertResponse(initSignCustomRequestWithAdvancedSignatureType);

    }

    @Test
    public void initiateSign_organisational_success()
            throws FrejaEidClientInternalException, IOException, InterruptedException, FrejaEidException {
        SignClient signClient =
                SignClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                        .setTestModeServerCustomUrl("http://localhost:" + MOCK_SERVICE_PORT)
                        .setTransactionContext(TransactionContext.ORGANISATIONAL).build();
        DataToSign dataToSign =
                DataToSign.create(Base64.encodeBase64String(dataToSignText.getBytes(StandardCharsets.UTF_8)));
        InitiateSignRequest initSignCustomRequestWithRequestedAttributes = InitiateSignRequest.createCustom()
                .setOrganisationId(ORGANISATION_ID)
                .setDataToSign(dataToSign)
                .setExpiry(Long.MAX_VALUE)
                .setMinRegistrationLevel(MinRegistrationLevel.EXTENDED)
                .setAttributesToReturn(ATTRIBUTES_TO_RETURN)
                .setPushNotification(pushNotification)
                .setTitle(title)
                .build();

        String initSignResponseString = jsonService.serializeToJson(initiateSignResponse);

        startMockServer(initSignCustomRequestWithRequestedAttributes, HttpStatusCode.OK.getCode(),
                        initSignResponseString);

        InitiateSignResponse response = signClient.initiateV1_1(initSignCustomRequestWithRequestedAttributes);
        stopServer();
        Assert.assertEquals(initiateSignResponse, response);
    }

    @Test
    public void getSignResult_sendRequestWithRelyingPartyId_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        SignResultRequest getOneSignResultRequest = SignResultRequest.create(REFERENCE, RELYING_PARTY_ID);
        String getOneResultResponseString = jsonService.serializeToJson(signResult);
        SignResultRequest expectedGetOneSignResultRequest = SignResultRequest.create(REFERENCE);
        startMockServer(expectedGetOneSignResultRequest, HttpStatusCode.OK.getCode(), getOneResultResponseString);

        SignResult response = signClient.getResult(getOneSignResultRequest);
        Assert.assertEquals(REFERENCE, response.getSignRef());
    }

    @Test
    public void getSignResult_sendRequestWithRelyingPartyId_withRequestedAttributes_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        SignResultRequest getOneSignResultRequest = SignResultRequest.create(REFERENCE, RELYING_PARTY_ID);
        String getOneResultResponseString = jsonService.serializeToJson(signResultWithRequestedAttributes);
        SignResultRequest expectedGetOneSignResultRequest = SignResultRequest.create(REFERENCE);
        startMockServer(expectedGetOneSignResultRequest, HttpStatusCode.OK.getCode(), getOneResultResponseString);

        SignResult response = signClient.getResult(getOneSignResultRequest);
        Assert.assertEquals(REFERENCE, response.getSignRef());
    }

    @Test
    public void getSignResult_sendRequestWithoutRelyingPartyId_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        SignResultRequest getOneSignResultRequest = SignResultRequest.create(REFERENCE);
        String getOneResultResponseString = jsonService.serializeToJson(signResult);

        startMockServer(getOneSignResultRequest, HttpStatusCode.OK.getCode(), getOneResultResponseString);

        SignResult response = signClient.getResult(getOneSignResultRequest);
        Assert.assertEquals(REFERENCE, response.getSignRef());
    }

    @Test
    public void getSignResults_sendRequestWithoutRelyingPartyId_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        SignResultsRequest getResultsRequest = SignResultsRequest.create();
        String getResultsResponseString = jsonService.serializeToJson(signResults);
        startMockServer(getResultsRequest, HttpStatusCode.OK.getCode(), getResultsResponseString);

        List<SignResult> response = signClient.getResults(getResultsRequest);
        Assert.assertEquals(REFERENCE, response.get(0).getSignRef());
    }

    @Test
    public void getSignResults_sendRequestWithRelyingPartyId_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        SignResultsRequest getResultsRequest = SignResultsRequest.create(RELYING_PARTY_ID);
        String getResultsResponseString = jsonService.serializeToJson(signResults);
        SignResultsRequest expectedGetResultsRequest = SignResultsRequest.create();
        startMockServer(expectedGetResultsRequest, HttpStatusCode.OK.getCode(), getResultsResponseString);

        List<SignResult> response = signClient.getResults(getResultsRequest);
        Assert.assertEquals(REFERENCE, response.get(0).getSignRef());
    }

    @Test
    public void cancelSign_sendRequestWithoutRelyingPartyId_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        CancelSignRequest cancelRequest = CancelSignRequest.create(REFERENCE);
        String responseString = jsonService.serializeToJson(EmptyFrejaResponse.INSTANCE);

        startMockServer(cancelRequest, HttpStatusCode.OK.getCode(), responseString);

        signClient.cancel(cancelRequest);
    }

    @Test
    public void cancelSign_sendRequestWithRelyingPartyId_success()
            throws FrejaEidClientInternalException, IOException, FrejaEidException {
        CancelSignRequest cancelRequest = CancelSignRequest.create(REFERENCE, RELYING_PARTY_ID);
        String responseString = jsonService.serializeToJson(EmptyFrejaResponse.INSTANCE);
        CancelSignRequest expectedCancelRequest = CancelSignRequest.create(REFERENCE);
        startMockServer(expectedCancelRequest, HttpStatusCode.OK.getCode(), responseString);

        signClient.cancel(cancelRequest);
    }
}
