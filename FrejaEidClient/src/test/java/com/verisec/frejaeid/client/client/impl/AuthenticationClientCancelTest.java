package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.beans.authentication.cancel.CancelAuthenticationRequest;
import com.verisec.frejaeid.client.beans.common.EmptyFrejaResponse;
import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.beans.general.SslSettings;
import com.verisec.frejaeid.client.client.api.AuthenticationClientApi;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.enums.FrejaEidErrorCode;
import com.verisec.frejaeid.client.enums.TransactionContext;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.http.HttpServiceApi;
import com.verisec.frejaeid.client.util.MethodUrl;
import com.verisec.frejaeid.client.util.RequestTemplate;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class AuthenticationClientCancelTest {

    private final HttpServiceApi httpServiceMock = Mockito.mock(HttpServiceApi.class);
    private static final String REFERENCE = "reference";
    private static final String RELYING_PARTY_ID = "relyingPartyId";

    @Test
    public void cancelAuthentication_relyingPartyIdNull_success() throws FrejaEidClientInternalException, FrejaEidException {
        AuthenticationClientApi authenticationClient = AuthenticationClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                .setHttpService(httpServiceMock)
                .setTransactionContext(TransactionContext.PERSONAL).build();
        CancelAuthenticationRequest cancelAuthenticationRequest = CancelAuthenticationRequest.create(REFERENCE);
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class), Mockito.any(RelyingPartyRequest.class), Mockito.eq(EmptyFrejaResponse.class), (String) Mockito.isNull())).thenReturn(EmptyFrejaResponse.INSTANCE);
        authenticationClient.cancel(cancelAuthenticationRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.AUTHENTICATION_CANCEL, RequestTemplate.CANCEL_AUTHENTICATION_TEMPLATE, cancelAuthenticationRequest, EmptyFrejaResponse.class, null);
    }

    @Test
    public void cancelAuthentication_personal_success() throws FrejaEidClientInternalException, FrejaEidException {
        AuthenticationClientApi authenticationClient = AuthenticationClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                .setHttpService(httpServiceMock)
                .setTransactionContext(TransactionContext.PERSONAL).build();
        CancelAuthenticationRequest cancelAuthenticationRequest = CancelAuthenticationRequest.create(REFERENCE, RELYING_PARTY_ID);
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class), Mockito.any(RelyingPartyRequest.class), Mockito.eq(EmptyFrejaResponse.class), Mockito.anyString())).thenReturn(EmptyFrejaResponse.INSTANCE);
        authenticationClient.cancel(cancelAuthenticationRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.AUTHENTICATION_CANCEL, RequestTemplate.CANCEL_AUTHENTICATION_TEMPLATE, cancelAuthenticationRequest, EmptyFrejaResponse.class, RELYING_PARTY_ID);
    }

    @Test
    public void cancelAuthentication_organisationl_success() throws FrejaEidClientInternalException, FrejaEidException {
        AuthenticationClientApi authenticationClient = AuthenticationClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                .setHttpService(httpServiceMock)
                .setTransactionContext(TransactionContext.ORGANISATIONAL).build();
        CancelAuthenticationRequest cancelAuthenticationRequest = CancelAuthenticationRequest.create(REFERENCE, RELYING_PARTY_ID);
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class), Mockito.any(RelyingPartyRequest.class), Mockito.eq(EmptyFrejaResponse.class), Mockito.anyString())).thenReturn(EmptyFrejaResponse.INSTANCE);
        authenticationClient.cancel(cancelAuthenticationRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.ORGANISATION_AUTHENTICATION_CANCEL, RequestTemplate.CANCEL_AUTHENTICATION_TEMPLATE, cancelAuthenticationRequest, EmptyFrejaResponse.class, RELYING_PARTY_ID);
    }

    @Test
    public void cancelAuthetnication_invalidReference_expectInvalidReferenceError() throws FrejaEidClientInternalException, FrejaEidException {
        CancelAuthenticationRequest cancelAuthenticationRequest = CancelAuthenticationRequest.create(REFERENCE, RELYING_PARTY_ID);
        try {
            AuthenticationClientApi authenticationClient = AuthenticationClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                    .setHttpService(httpServiceMock)
                    .setTransactionContext(TransactionContext.PERSONAL).build();

            Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class), Mockito.any(RelyingPartyRequest.class), Mockito.eq(EmptyFrejaResponse.class), Mockito.anyString())).thenThrow(new FrejaEidException(FrejaEidErrorCode.INVALID_REFERENCE.getMessage(), FrejaEidErrorCode.INVALID_REFERENCE.getCode()));
            authenticationClient.cancel(cancelAuthenticationRequest);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidException rpEx) {
            Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.AUTHENTICATION_CANCEL, RequestTemplate.CANCEL_AUTHENTICATION_TEMPLATE, cancelAuthenticationRequest, EmptyFrejaResponse.class, RELYING_PARTY_ID);
            Assert.assertEquals(1100, rpEx.getErrorCode());
            Assert.assertEquals("Invalid reference (for example, nonexistent or expired).", rpEx.getLocalizedMessage());
        }
    }
}
