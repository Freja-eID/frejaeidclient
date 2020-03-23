package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.beans.general.SslSettings;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.beans.sign.init.DataToSign;
import com.verisec.frejaeid.client.beans.sign.init.InitiateSignRequest;
import com.verisec.frejaeid.client.beans.sign.init.InitiateSignResponse;
import com.verisec.frejaeid.client.beans.sign.init.PushNotification;
import com.verisec.frejaeid.client.client.api.SignClientApi;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.AttributeToReturn;
import com.verisec.frejaeid.client.enums.Country;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.enums.MinRegistrationLevel;
import com.verisec.frejaeid.client.enums.FrejaEidErrorCode;
import com.verisec.frejaeid.client.enums.TransactionContext;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.http.HttpServiceApi;
import com.verisec.frejaeid.client.util.MethodUrl;
import com.verisec.frejaeid.client.util.RequestTemplate;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SignClientInitSignTest {

    private final HttpServiceApi httpServiceMock = Mockito.mock(HttpServiceApi.class);

    private static final String EMAIL = "eid.demo.verisec@gmail.com";
    private static final String SSN = "199207295578";
    private static final String SIGN_REFERENCE = "123456789123456789";
    private static final String RELYING_PARTY_ID = "verisec_integrator";

    private MinRegistrationLevel minRegistrationLevel;
    private String title;
    private String text;
    private String pushNotificationTitle;
    private String pushNotificationText;
    private PushNotification pushNotification;

    private Long expiry;
    private final DataToSign dataToSign = DataToSign.create("SGVsbG8=");

    @Before
    public void initDefaultRequest() throws FrejaEidClientInternalException {
        minRegistrationLevel = MinRegistrationLevel.BASIC;
        title = "Sign transaction title";
        text = "Sign transaction text";
        pushNotificationTitle = "Sign notification title";
        pushNotificationText = "Sign notification text";
        pushNotification = PushNotification.create(pushNotificationTitle, pushNotificationText);
        expiry = TimeUnit.MINUTES.toMillis(6);
    }

    @Test
    public void initSign_defaultRequests_personal_expectSuccess() throws FrejaEidClientInternalException, FrejaEidException {
        InitiateSignResponse expectedResponse = new InitiateSignResponse(SIGN_REFERENCE);
        SignClientApi signClient = SignClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                .setHttpService(httpServiceMock)
                .setTransactionContext(TransactionContext.PERSONAL).build();
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class), Mockito.any(RelyingPartyRequest.class), Mockito.eq(InitiateSignResponse.class), (String) Mockito.isNull())).thenReturn(expectedResponse);

        InitiateSignRequest initiateSignDefaultEmailRequest = InitiateSignRequest.createDefaultWithEmail(EMAIL, title, text);
        InitiateSignRequest initiateSignDefaultSsnRequest = InitiateSignRequest.createDefaultWithSsn(SsnUserInfo.create(Country.SWEDEN, SSN), title, text);

        String reference = signClient.initiate(initiateSignDefaultEmailRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.SIGN_INIT, RequestTemplate.INIT_SIGN_TEMPLATE, initiateSignDefaultEmailRequest, InitiateSignResponse.class, null);
        Assert.assertEquals(SIGN_REFERENCE, reference);

        reference = signClient.initiate(initiateSignDefaultSsnRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.SIGN_INIT, RequestTemplate.INIT_SIGN_TEMPLATE, initiateSignDefaultSsnRequest, InitiateSignResponse.class, null);
        Assert.assertEquals(SIGN_REFERENCE, reference);
    }

    @Test
    public void initSign_defaultRequests_organisational_expectSuccess() throws FrejaEidClientInternalException, FrejaEidException {
        InitiateSignResponse expectedResponse = new InitiateSignResponse(SIGN_REFERENCE);
        SignClientApi signClient = SignClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                .setHttpService(httpServiceMock)
                .setTransactionContext(TransactionContext.ORGANISATIONAL).build();
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class), Mockito.any(RelyingPartyRequest.class), Mockito.eq(InitiateSignResponse.class), (String) Mockito.isNull())).thenReturn(expectedResponse);

        InitiateSignRequest initiateSignDefaultEmailRequest = InitiateSignRequest.createDefaultWithEmail(EMAIL, title, text);
        InitiateSignRequest initiateSignDefaultSsnRequest = InitiateSignRequest.createDefaultWithSsn(SsnUserInfo.create(Country.SWEDEN, SSN), title, text);

        String reference = signClient.initiate(initiateSignDefaultEmailRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.ORGANISATION_SIGN_INIT, RequestTemplate.INIT_SIGN_TEMPLATE, initiateSignDefaultEmailRequest, InitiateSignResponse.class, null);
        Assert.assertEquals(SIGN_REFERENCE, reference);

        reference = signClient.initiate(initiateSignDefaultSsnRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.ORGANISATION_SIGN_INIT, RequestTemplate.INIT_SIGN_TEMPLATE, initiateSignDefaultSsnRequest, InitiateSignResponse.class, null);
        Assert.assertEquals(SIGN_REFERENCE, reference);
    }

    @Test
    public void initSign_customRequests_expectSuccess() throws FrejaEidClientInternalException, FrejaEidException {
        InitiateSignResponse expectedResponse = new InitiateSignResponse(SIGN_REFERENCE);
        SignClientApi signClient = SignClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                .setHttpService(httpServiceMock)
                .setTransactionContext(TransactionContext.PERSONAL).build();
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class), Mockito.any(RelyingPartyRequest.class), Mockito.eq(InitiateSignResponse.class), Mockito.anyString())).thenReturn(expectedResponse);

        InitiateSignRequest initiateSignRequest = InitiateSignRequest.createCustom()
                .setEmail(EMAIL)
                .setDataToSign(dataToSign)
                .setExpiry(expiry)
                .setMinRegistrationLevel(minRegistrationLevel)
                .setPushNotification(pushNotification)
                .setAttributesToReturn(AttributeToReturn.CUSTOM_IDENTIFIER, AttributeToReturn.BASIC_USER_INFO, AttributeToReturn.DATE_OF_BIRTH, AttributeToReturn.EMAIL_ADDRESS, AttributeToReturn.INTEGRATOR_SPECIFIC_USER_ID, AttributeToReturn.RELYING_PARTY_USER_ID, AttributeToReturn.SSN, AttributeToReturn.ADDRESSES)
                .setTitle(title)
                .setRelyingPartyId(RELYING_PARTY_ID)
                .build();

        String reference = signClient.initiate(initiateSignRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.SIGN_INIT, RequestTemplate.INIT_SIGN_TEMPLATE, initiateSignRequest, InitiateSignResponse.class, RELYING_PARTY_ID);
        Assert.assertEquals(SIGN_REFERENCE, reference);

    }

    @Test
    public void initSign_customRequest_minRegistrationLevelNull_organisational_expectSuccess() throws FrejaEidClientInternalException, FrejaEidException {
        InitiateSignResponse expectedResponse = new InitiateSignResponse(SIGN_REFERENCE);
        SignClientApi signClient = SignClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                .setHttpService(httpServiceMock)
                .setTransactionContext(TransactionContext.ORGANISATIONAL).build();
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class), Mockito.any(RelyingPartyRequest.class), Mockito.eq(InitiateSignResponse.class), Mockito.anyString())).thenReturn(expectedResponse);
        InitiateSignRequest initiateSignRequest = InitiateSignRequest.createCustom()
                .setPhoneNumber(EMAIL)
                .setDataToSign(dataToSign)
                .setExpiry(expiry)
                .setMinRegistrationLevel(MinRegistrationLevel.EXTENDED)
                .setPushNotification(PushNotification.create(pushNotificationTitle, pushNotificationText))
                .setTitle(title)
                .setRelyingPartyId(RELYING_PARTY_ID)
                .build();
        String reference = signClient.initiate(initiateSignRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.ORGANISATION_SIGN_INIT, RequestTemplate.INIT_SIGN_TEMPLATE, initiateSignRequest, InitiateSignResponse.class, RELYING_PARTY_ID);
        Assert.assertEquals(SIGN_REFERENCE, reference);
    }

    @Test
    public void initSign_userInfoTelefonNumber_expectError() throws FrejaEidClientInternalException, FrejaEidException {
        InitiateSignRequest initiateSignRequest = InitiateSignRequest.createCustom()
                .setPhoneNumber(EMAIL)
                .setDataToSign(dataToSign)
                .setExpiry(expiry)
                .setMinRegistrationLevel(minRegistrationLevel)
                .setPushNotification(PushNotification.create(pushNotificationTitle, pushNotificationText))
                .setTitle(title)
                .setRelyingPartyId(RELYING_PARTY_ID)
                .build();
        try {
            SignClientApi signClient = SignClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                    .setHttpService(httpServiceMock)
                    .setTransactionContext(TransactionContext.PERSONAL).build();
            Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class), Mockito.any(RelyingPartyRequest.class), Mockito.eq(InitiateSignResponse.class), Mockito.anyString())).thenThrow(new FrejaEidException(FrejaEidErrorCode.INVALID_USER_INFO.getMessage(), FrejaEidErrorCode.INVALID_USER_INFO.getCode()));

            signClient.initiate(initiateSignRequest);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidException rpEx) {
            Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.SIGN_INIT, RequestTemplate.INIT_SIGN_TEMPLATE, initiateSignRequest, InitiateSignResponse.class, RELYING_PARTY_ID);
            Assert.assertEquals(1002, rpEx.getErrorCode());
            Assert.assertEquals("Invalid or missing userInfo.", rpEx.getLocalizedMessage());
        }
    }

}
