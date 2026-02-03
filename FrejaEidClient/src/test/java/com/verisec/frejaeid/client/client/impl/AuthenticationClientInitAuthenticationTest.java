package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.beans.authentication.init.InitiateAuthenticationRequest;
import com.verisec.frejaeid.client.beans.authentication.init.InitiateAuthenticationResponse;
import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.client.api.AuthenticationClientApi;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.*;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.http.HttpServiceApi;
import com.verisec.frejaeid.client.util.MethodUrl;
import com.verisec.frejaeid.client.util.RequestTemplate;
import org.apache.http.HttpEntity;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class AuthenticationClientInitAuthenticationTest {

    private final HttpServiceApi httpServiceMock = Mockito.mock(HttpServiceApi.class);
    private static final String EMAIL = "eid.demo.verisec@gmail.com";
    private static final String SSN = "199207295578";
    private static final String REFERENCE = "123456789012345678";
    private static final String QR_CODE_SECRET = "qrCodeSecret";
    private static final String RELYING_PARTY_ID = "relyingPartyId";
    private static final String ORGANISATION_ID = "orgId";
    private static final Country COUNTRY = Country.SWEDEN;
    private static final AttributeToReturn[] ATTRIBUTES_TO_RETURN = AttributeToReturn.values();
    private static final String QR_CODE_GENERATION_URL_PREFIX = "https://resources.test.frejaeid.com/qrcode/generate";
    protected static final String UPI = "5633-823597-7862";

    @Test
    public void initiateAuthenticationV1_1_userInfoTypeEmail_success() throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createDefaultWithEmail(EMAIL);
        initiateAuthenticationV1_1_relyingPartyNull_success(initiateAuthenticationRequest);
    }

    @Test
    public void initiateAuthenticationV1_1_userInfoTypeUpi_success() throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createDefaultWithUpi(UPI);
        initiateAuthenticationV1_1_relyingPartyNull_success(initiateAuthenticationRequest);
    }

    @Test
    public void generateQRCode_expectSuccess() throws FrejaEidClientInternalException, FrejaEidException, IOException {
        byte[] byteArray = null;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(REFERENCE.getBytes());
             ByteArrayInputStream expectedBytesStream = new ByteArrayInputStream(REFERENCE.getBytes())
        ) {
            byte[] expectedBytesResult = readAllBytes(expectedBytesStream);
            AuthenticationClientApi authenticationClient =
                    AuthenticationClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                            .setHttpService(httpServiceMock)
                            .setTransactionContext(TransactionContext.PERSONAL).build();
            HttpEntity httpResponseEntity = Mockito.mock(HttpEntity.class);
            Mockito.when(httpResponseEntity.getContent()).thenReturn(inputStream);
            Mockito.when(httpServiceMock.httpGet(Mockito.matches(QR_CODE_GENERATION_URL_PREFIX), Mockito.<String, String>anyMap()))
                    .thenReturn(expectedBytesResult);

            byteArray = authenticationClient.generateQRCodeForAuthentication(REFERENCE);
            Assert.assertEquals(Arrays.toString(byteArray), Arrays.toString(expectedBytesResult));
        }
    }

    @Test
    public void initiateAuthenticationV1_1_expectError() throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createCustom().setPhoneNumber(EMAIL)
                        .setRelyingPartyId(RELYING_PARTY_ID).build();
        try {
            AuthenticationClientApi authenticationClient =
                    AuthenticationClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                            .setHttpService(httpServiceMock)
                            .setTransactionContext(TransactionContext.PERSONAL).build();
            Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                              Mockito.any(RelyingPartyRequest.class),
                                              Mockito.eq(InitiateAuthenticationResponse.class), Mockito.anyString()))
                    .thenThrow(new FrejaEidException(FrejaEidErrorCode.INVALID_USER_INFO.getMessage(),
                                                     FrejaEidErrorCode.INVALID_USER_INFO.getCode()));
            authenticationClient.initiateV1_1(initiateAuthenticationRequest);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidException rpEx) {
            Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.AUTHENTICATION_INIT,
                                                 RequestTemplate.INIT_AUTHENTICATION, initiateAuthenticationRequest,
                                                 InitiateAuthenticationResponse.class, RELYING_PARTY_ID);
            Assert.assertEquals("Invalid error", 1002, rpEx.getErrorCode());
            Assert.assertEquals("Invalid error", "Invalid or missing userInfo.", rpEx.getLocalizedMessage());
        }
    }

    @Test
    public void initiateAuthenticationV1_1_userInfoTypeSsn_success() throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createDefaultWithSsn(SsnUserInfo.create(COUNTRY, SSN));
        initiateAuthenticationV1_1_relyingPartyNull_success(initiateAuthenticationRequest);
    }

    @Test
    public void initiateAuthenticationV1_1_userInfoTypeEmail_requestedAttributes_success()
            throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createCustom().setEmail(EMAIL)
                        .setAttributesToReturn(ATTRIBUTES_TO_RETURN).setRelyingPartyId(RELYING_PARTY_ID).build();
        initiateAuthenticationV1_1_personalContext_relyingPartyNotNull_success(initiateAuthenticationRequest);
    }

    @Test
    public void initiateAuthenticationV1_1_userInfoTypeSsn_requestedAttributes_success()
            throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createCustom().setSsn(SsnUserInfo.create(COUNTRY, SSN))
                        .setAttributesToReturn(ATTRIBUTES_TO_RETURN).setRelyingPartyId(RELYING_PARTY_ID).build();
        initiateAuthenticationV1_1_personalContext_relyingPartyNotNull_success(initiateAuthenticationRequest);
    }

    @Test
    public void initiateAuthenticationV1_1_userInfoTypePhoneNumber_requestedAttributes_success()
            throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createCustom().setPhoneNumber(EMAIL)
                        .setAttributesToReturn(ATTRIBUTES_TO_RETURN).setRelyingPartyId(RELYING_PARTY_ID).build();
        initiateAuthenticationV1_1_personalContext_relyingPartyNotNull_success(initiateAuthenticationRequest);
    }

    @Test
    public void initiateAuthenticationV1_1_userInfoTypeOrganisationId_requestedAttributes_success()
            throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createCustom().setOrganisationId(ORGANISATION_ID)
                        .setAttributesToReturn(ATTRIBUTES_TO_RETURN).setRelyingPartyId(RELYING_PARTY_ID).build();
        initiateAuthenticationV1_1_relyingPartyNotNull_success(initiateAuthenticationRequest, TransactionContext.ORGANISATIONAL);
    }

    @Test
    public void initiateAuthenticationV1_1_minRegistrationLevelBasic_success() throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createCustom().setEmail(EMAIL).setAttributesToReturn(ATTRIBUTES_TO_RETURN)
                        .setRelyingPartyId(RELYING_PARTY_ID).build();
        initiateAuthenticationV1_1_personalContext_relyingPartyNotNull_success(initiateAuthenticationRequest);
    }

    @Test
    public void initiateAuthenticationV1_1_minRegistrationLevelPlus_success() throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createCustom().setEmail(EMAIL)
                        .setAttributesToReturn(AttributeToReturn.CUSTOM_IDENTIFIER)
                        .setMinRegistrationLevel(MinRegistrationLevel.PLUS).build();
        initiateAuthenticationV1_1_relyingPartyNull_success(initiateAuthenticationRequest);
    }

    @Test
    public void initiateAuthenticationV1_1_userInfoTypeInferred_requestedAttributes_success()
            throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createCustom().setInferred().build();
        initiateAuthenticationV1_1_relyingPartyNull_success(initiateAuthenticationRequest);
    }

    @Test
    public void initiateAuthentication_userInfoTypeEmail_success() throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createDefaultWithEmail(EMAIL);
        initiateAuthentication_relyingPartyNull_success(initiateAuthenticationRequest);
    }

    @Test
    public void initiateAuthentication_expectError() throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createCustom().setPhoneNumber(EMAIL)
                        .setRelyingPartyId(RELYING_PARTY_ID).build();
        try {
            AuthenticationClientApi authenticationClient =
                    AuthenticationClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                            .setHttpService(httpServiceMock)
                            .setTransactionContext(TransactionContext.PERSONAL).build();
            Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                              Mockito.any(RelyingPartyRequest.class),
                                              Mockito.eq(InitiateAuthenticationResponse.class), Mockito.anyString()))
                    .thenThrow(new FrejaEidException(FrejaEidErrorCode.INVALID_USER_INFO.getMessage(),
                                                     FrejaEidErrorCode.INVALID_USER_INFO.getCode()));
            authenticationClient.initiateV1_1(initiateAuthenticationRequest);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidException rpEx) {
            Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.AUTHENTICATION_INIT,
                                                 RequestTemplate.INIT_AUTHENTICATION, initiateAuthenticationRequest,
                                                 InitiateAuthenticationResponse.class, RELYING_PARTY_ID);
            Assert.assertEquals("Invalid error", 1002, rpEx.getErrorCode());
            Assert.assertEquals("Invalid error", "Invalid or missing userInfo.", rpEx.getLocalizedMessage());
        }
    }

    @Test
    public void initiateAuthentication_userInfoTypeSsn_success() throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createDefaultWithSsn(SsnUserInfo.create(COUNTRY, SSN));
        initiateAuthentication_relyingPartyNull_success(initiateAuthenticationRequest);
    }

    @Test
    public void initiateAuthentication_userInfoTypeEmail_requestedAttributes_success()
            throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createCustom().setEmail(EMAIL)
                        .setAttributesToReturn(ATTRIBUTES_TO_RETURN).setRelyingPartyId(RELYING_PARTY_ID).build();
        initiateAuthentication_personalContext_relyingPartyNotNull_success(initiateAuthenticationRequest);
    }

    @Test
    public void initiateAuthentication_userInfoTypeSsn_requestedAttributes_success()
            throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createCustom().setSsn(SsnUserInfo.create(COUNTRY, SSN))
                        .setAttributesToReturn(ATTRIBUTES_TO_RETURN).setRelyingPartyId(RELYING_PARTY_ID).build();
        initiateAuthentication_personalContext_relyingPartyNotNull_success(initiateAuthenticationRequest);
    }

    @Test
    public void initiateAuthentication_userInfoTypePhoneNumber_requestedAttributes_success()
            throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createCustom().setPhoneNumber(EMAIL)
                        .setAttributesToReturn(ATTRIBUTES_TO_RETURN).setRelyingPartyId(RELYING_PARTY_ID).build();
        initiateAuthentication_personalContext_relyingPartyNotNull_success(initiateAuthenticationRequest);
    }

    @Test
    public void initiateAuthentication_userInfoTypeOrganisationId_requestedAttributes_success()
            throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createCustom().setOrganisationId(ORGANISATION_ID)
                        .setAttributesToReturn(ATTRIBUTES_TO_RETURN).setRelyingPartyId(RELYING_PARTY_ID).build();
        initiateAuthentication_relyingPartyNotNull_success(initiateAuthenticationRequest, TransactionContext.ORGANISATIONAL);
    }

    @Test
    public void initiateAuthentication_minRegistrationLevelBasic_success() throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createCustom().setEmail(EMAIL).setAttributesToReturn(ATTRIBUTES_TO_RETURN)
                        .setRelyingPartyId(RELYING_PARTY_ID).build();
        initiateAuthentication_personalContext_relyingPartyNotNull_success(initiateAuthenticationRequest);
    }

    @Test
    public void initiateAuthentication_minRegistrationLevelPlus_success() throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createCustom().setEmail(EMAIL)
                        .setAttributesToReturn(AttributeToReturn.CUSTOM_IDENTIFIER)
                        .setMinRegistrationLevel(MinRegistrationLevel.PLUS).build();
        initiateAuthentication_relyingPartyNull_success(initiateAuthenticationRequest);
    }

    @Test
    public void initiateAuthentication_userInfoTypeInferred_requestedAttributes_success()
            throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createCustom().setInferred().build();
        initiateAuthentication_relyingPartyNull_success(initiateAuthenticationRequest);
    }

    private void initiateAuthenticationV1_1_relyingPartyNull_success(InitiateAuthenticationRequest initiateAuthenticationRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationResponse expectedResponse = new InitiateAuthenticationResponse(REFERENCE, QR_CODE_SECRET);
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(InitiateAuthenticationResponse.class),
                                          (String) Mockito.isNull())).thenReturn(expectedResponse);
        AuthenticationClientApi authenticationClient =
                AuthenticationClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                        .setHttpService(httpServiceMock)
                        .setTransactionContext(TransactionContext.PERSONAL).build();
        InitiateAuthenticationResponse response = authenticationClient.initiateV1_1(initiateAuthenticationRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.AUTHENTICATION_INIT,
                                             RequestTemplate.INIT_AUTHENTICATION, initiateAuthenticationRequest,
                                             InitiateAuthenticationResponse.class, null);
        Assert.assertEquals(expectedResponse, response);
    }

    private void initiateAuthenticationV1_1_personalContext_relyingPartyNotNull_success(
            InitiateAuthenticationRequest initiateAuthenticationRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        initiateAuthenticationV1_1_relyingPartyNotNull_success(initiateAuthenticationRequest, TransactionContext.PERSONAL);
    }

    private void initiateAuthenticationV1_1_relyingPartyNotNull_success(InitiateAuthenticationRequest initiateAuthenticationRequest,
                                                                    TransactionContext transactionContext)
            throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationResponse expectedResponse = new InitiateAuthenticationResponse(REFERENCE, QR_CODE_SECRET);
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(InitiateAuthenticationResponse.class), Mockito.anyString()))
                .thenReturn(expectedResponse);
        AuthenticationClientApi authenticationClient =
                AuthenticationClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                        .setHttpService(httpServiceMock)
                        .setTransactionContext(transactionContext).build();
        InitiateAuthenticationResponse response = authenticationClient.initiateV1_1(initiateAuthenticationRequest);
        if (transactionContext.equals(TransactionContext.PERSONAL)) {
            Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.AUTHENTICATION_INIT,
                                                 RequestTemplate.INIT_AUTHENTICATION, initiateAuthenticationRequest,
                                                 InitiateAuthenticationResponse.class, RELYING_PARTY_ID);
        } else {
            Mockito.verify(httpServiceMock)
                    .send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.ORGANISATION_AUTHENTICATION_INIT,
                          RequestTemplate.INIT_AUTHENTICATION, initiateAuthenticationRequest,
                          InitiateAuthenticationResponse.class, RELYING_PARTY_ID);
        }

        Assert.assertEquals(expectedResponse, response);
    }


    private void initiateAuthentication_relyingPartyNull_success(InitiateAuthenticationRequest initiateAuthenticationRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationResponse expectedResponse = new InitiateAuthenticationResponse(REFERENCE, QR_CODE_SECRET);
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(InitiateAuthenticationResponse.class),
                                          (String) Mockito.isNull())).thenReturn(expectedResponse);
        AuthenticationClientApi authenticationClient =
                AuthenticationClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                        .setHttpService(httpServiceMock)
                        .setTransactionContext(TransactionContext.PERSONAL).build();
        String reference = authenticationClient.initiate(initiateAuthenticationRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.AUTHENTICATION_INIT,
                                             RequestTemplate.INIT_AUTHENTICATION, initiateAuthenticationRequest,
                                             InitiateAuthenticationResponse.class, null);
        Assert.assertEquals(REFERENCE, reference);
    }

    private void initiateAuthentication_personalContext_relyingPartyNotNull_success(
            InitiateAuthenticationRequest initiateAuthenticationRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        initiateAuthentication_relyingPartyNotNull_success(initiateAuthenticationRequest, TransactionContext.PERSONAL);
    }

    private void initiateAuthentication_relyingPartyNotNull_success(InitiateAuthenticationRequest initiateAuthenticationRequest,
                                                                        TransactionContext transactionContext)
            throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationResponse expectedResponse = new InitiateAuthenticationResponse(REFERENCE, QR_CODE_SECRET);
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(InitiateAuthenticationResponse.class), Mockito.anyString()))
                .thenReturn(expectedResponse);
        AuthenticationClientApi authenticationClient =
                AuthenticationClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                        .setHttpService(httpServiceMock)
                        .setTransactionContext(transactionContext).build();
        String reference = authenticationClient.initiate(initiateAuthenticationRequest);
        if (transactionContext.equals(TransactionContext.PERSONAL)) {
            Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.AUTHENTICATION_INIT,
                                                 RequestTemplate.INIT_AUTHENTICATION, initiateAuthenticationRequest,
                                                 InitiateAuthenticationResponse.class, RELYING_PARTY_ID);
        } else {
            Mockito.verify(httpServiceMock)
                    .send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.ORGANISATION_AUTHENTICATION_INIT,
                          RequestTemplate.INIT_AUTHENTICATION, initiateAuthenticationRequest,
                          InitiateAuthenticationResponse.class, RELYING_PARTY_ID);
        }

        Assert.assertEquals(REFERENCE, reference);
    }

    private static byte[] readAllBytes(InputStream inputStream) throws FrejaEidClientInternalException {
        final int bufferLengthInKB = 1024;
        byte[] buffer = new byte[bufferLengthInKB];
        int line;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            while ((line = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, line);
            }
            return outputStream.toByteArray();
        } catch (IOException ex) {
            throw new FrejaEidClientInternalException("Failed to read bytes returned in http response", ex);
        }
    }
}
