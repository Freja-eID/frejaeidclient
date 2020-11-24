package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.beans.authentication.init.InitiateAuthenticationResponse;
import com.verisec.frejaeid.client.beans.authentication.init.InitiateAuthenticationRequest;
import com.verisec.frejaeid.client.beans.general.SslSettings;
import com.verisec.frejaeid.client.client.api.AuthenticationClientApi;
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
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class AuthenticationClientInitAuthenticationTest {

    private final HttpServiceApi httpServiceMock = Mockito.mock(HttpServiceApi.class);
    private static final String EMAIL = "eid.demo.verisec@gmail.com";
    private static final String SSN = "199207295578";
    private static final String REFERENCE = "123456789012345678";
    private static final String RELYING_PARTY_ID = "relyingPartyId";
    private static final String ORGANISATION_ID = "orgId";
    private static final Country COUNTRY = Country.SWEDEN;
    private static final AttributeToReturn[] ATTRIBUTES_TO_RETURN = AttributeToReturn.values();

    @Test
    public void initAuth_userInfoTypeEmail_success() throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createDefaultWithEmail(EMAIL);
        initAuth_relyingPartyNull_success(initiateAuthenticationRequest);
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
            Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.AUTHENTICATION_INIT,
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
        InitiateAuthenticationResponse expectedResponse = new InitiateAuthenticationResponse(REFERENCE);
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class),
                                          Mockito.any(RelyingPartyRequest.class),
                                          Mockito.eq(InitiateAuthenticationResponse.class),
                                          (String) Mockito.isNull())).thenReturn(expectedResponse);
        AuthenticationClientApi authenticationClient =
                AuthenticationClient.create(TestUtil.getDefaultSslSettings(), FrejaEnvironment.TEST)
                        .setHttpService(httpServiceMock)
                        .setTransactionContext(TransactionContext.PERSONAL).build();
        String reference = authenticationClient.initiate(initiateAuthenticationRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.AUTHENTICATION_INIT,
                                             RequestTemplate.INIT_AUTHENTICATION, initiateAuthenticationRequest,
                                             InitiateAuthenticationResponse.class, null);
        Assert.assertEquals(REFERENCE, reference);
    }

    private void initAuth_personalContext_relyingPartyNotNull_success(
            InitiateAuthenticationRequest initiateAuthenticationRequest)
            throws FrejaEidClientInternalException, FrejaEidException {
        initAuth_relyingPartyNotNull_success(initiateAuthenticationRequest, TransactionContext.PERSONAL);
    }

    private void initAuth_relyingPartyNotNull_success(InitiateAuthenticationRequest initiateAuthenticationRequest,
                                                      TransactionContext transactionContext)
            throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAuthenticationResponse expectedResponse = new InitiateAuthenticationResponse(REFERENCE);
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
            Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.AUTHENTICATION_INIT,
                                                 RequestTemplate.INIT_AUTHENTICATION, initiateAuthenticationRequest,
                                                 InitiateAuthenticationResponse.class, RELYING_PARTY_ID);
        } else {
            Mockito.verify(httpServiceMock)
                    .send(FrejaEnvironment.TEST.getUrl() + MethodUrl.ORGANISATION_AUTHENTICATION_INIT,
                          RequestTemplate.INIT_AUTHENTICATION, initiateAuthenticationRequest,
                          InitiateAuthenticationResponse.class, RELYING_PARTY_ID);
        }

        Assert.assertEquals(REFERENCE, reference);
    }
}
