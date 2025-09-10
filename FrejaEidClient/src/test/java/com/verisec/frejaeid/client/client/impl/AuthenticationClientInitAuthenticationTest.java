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

    @Test
    public void initAuth_userInfoTypeEmail_success() throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createDefaultWithEmail(EMAIL);
        initAuth_relyingPartyNull_success(initiateAuthenticationRequest);
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
    public void initAuth_expectError() throws FrejaEidClientInternalException, FrejaEidException {
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
            authenticationClient.initiate(initiateAuthenticationRequest);
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
    public void initAuth_userInfoTypeSsn_success() throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createDefaultWithSsn(SsnUserInfo.create(COUNTRY, SSN));
        initAuth_relyingPartyNull_success(initiateAuthenticationRequest);
    }

    @Test
    public void initAuth_userInfoTypeEmail_requestedAttributes_success()
            throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createCustom().setEmail(EMAIL)
                        .setAttributesToReturn(ATTRIBUTES_TO_RETURN).setRelyingPartyId(RELYING_PARTY_ID).build();
        initAuth_personalContext_relyingPartyNotNull_success(initiateAuthenticationRequest);
    }

    @Test
    public void initAuth_userInfoTypeSsn_requestedAttributes_success()
            throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createCustom().setSsn(SsnUserInfo.create(COUNTRY, SSN))
                        .setAttributesToReturn(ATTRIBUTES_TO_RETURN).setRelyingPartyId(RELYING_PARTY_ID).build();
        initAuth_personalContext_relyingPartyNotNull_success(initiateAuthenticationRequest);
    }

    @Test
    public void initAuth_userInfoTypePhoneNumber_requestedAttributes_success()
            throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createCustom().setPhoneNumber(EMAIL)
                        .setAttributesToReturn(ATTRIBUTES_TO_RETURN).setRelyingPartyId(RELYING_PARTY_ID).build();
        initAuth_personalContext_relyingPartyNotNull_success(initiateAuthenticationRequest);
    }

    @Test
    public void initAuth_userInfoTypeOrganisationId_requestedAttributes_success()
            throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createCustom().setOrganisationId(ORGANISATION_ID)
                        .setAttributesToReturn(ATTRIBUTES_TO_RETURN).setRelyingPartyId(RELYING_PARTY_ID).build();
        initAuth_relyingPartyNotNull_success(initiateAuthenticationRequest, TransactionContext.ORGANISATIONAL);
    }

    @Test
    public void initAuth_minRegistrationLevelBasic_success() throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createCustom().setEmail(EMAIL).setAttributesToReturn(ATTRIBUTES_TO_RETURN)
                        .setRelyingPartyId(RELYING_PARTY_ID).build();
        initAuth_personalContext_relyingPartyNotNull_success(initiateAuthenticationRequest);
    }

    @Test
    public void initAuth_minRegistrationLevelPlus_success() throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createCustom().setEmail(EMAIL)
                        .setAttributesToReturn(AttributeToReturn.CUSTOM_IDENTIFIER)
                        .setMinRegistrationLevel(MinRegistrationLevel.PLUS).build();
        initAuth_relyingPartyNull_success(initiateAuthenticationRequest);
    }

    @Test
    public void initAuth_userInfoTypeInferred_requestedAttributes_success()
            throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createCustom().setInferred().build();
        initAuth_relyingPartyNull_success(initiateAuthenticationRequest);
    }

    private void initAuth_relyingPartyNull_success(InitiateAuthenticationRequest initiateAuthenticationRequest)
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
        InitiateAuthenticationResponse response = authenticationClient.initiate(initiateAuthenticationRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getServiceUrl() + MethodUrl.AUTHENTICATION_INIT,
                                             RequestTemplate.INIT_AUTHENTICATION, initiateAuthenticationRequest,
                                             InitiateAuthenticationResponse.class, null);
        Assert.assertEquals(expectedResponse, response);
    }

    private void initAuth_personalContext_relyingPartyNotNull_success(
            InitiateAuthenticationRequest initiateAuthenticationRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        initAuth_relyingPartyNotNull_success(initiateAuthenticationRequest, TransactionContext.PERSONAL);
    }

    private void initAuth_relyingPartyNotNull_success(InitiateAuthenticationRequest initiateAuthenticationRequest,
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
        InitiateAuthenticationResponse response = authenticationClient.initiate(initiateAuthenticationRequest);
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
