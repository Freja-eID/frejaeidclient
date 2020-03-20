package com.verisec.frejaeid.client.client.impl;

import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.beans.general.OrganisationId;
import com.verisec.frejaeid.client.beans.general.SslSettings;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.beans.organisationid.init.InitiateAddOrganisationIdRequest;
import com.verisec.frejaeid.client.beans.organisationid.init.InitiateAddOrganisationIdResponse;
import com.verisec.frejaeid.client.client.api.OrganisationIdClientApi;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.Country;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.enums.MinRegistrationLevel;
import com.verisec.frejaeid.client.enums.FrejaEidErrorCode;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.http.HttpServiceApi;
import com.verisec.frejaeid.client.util.MethodUrl;
import com.verisec.frejaeid.client.util.RequestTemplate;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class OrganisationIdClientInitAddOrganisationIdTest {

    private final HttpServiceApi httpServiceMock = Mockito.mock(HttpServiceApi.class);

    private static final String EMAIL = "eid.demo.verisec@gmail.com";
    private static final String SSN = "199207295578";
    private static final String REFERENCE = "123456789123456789";
    private static final String RELYING_PARTY_ID = "verisec_integrator";
    private static final String ORGANISATION_ID_TITLE = "OrgananisationId title";
    private static final String IDENTIFIER_NAME = "Identifier name";
    private static final String IDENTIFIER = "identifier";

    private final MinRegistrationLevel minRegistrationLevel = MinRegistrationLevel.EXTENDED;
    private final Long expiry = TimeUnit.MINUTES.toMillis(6);

    @Test
    public void initAddOrganisationId_defaultRequests_expectSuccess() throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAddOrganisationIdResponse expectedResponse = new InitiateAddOrganisationIdResponse(REFERENCE);
        OrganisationIdClientApi organisationIdClient = OrganisationIdClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                .setHttpService(httpServiceMock)
                .build();
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class), Mockito.any(RelyingPartyRequest.class), Mockito.eq(InitiateAddOrganisationIdResponse.class), (String) Mockito.isNull())).thenReturn(expectedResponse);

        InitiateAddOrganisationIdRequest initiateAddOrganisationIdDefaultEmailRequest = InitiateAddOrganisationIdRequest.createDefaultWithEmail(EMAIL, OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER));
        InitiateAddOrganisationIdRequest initiateAddOrganisationIdDefaultSsnRequest = InitiateAddOrganisationIdRequest.createDefaultWithSsn(new SsnUserInfo(Country.SWEDEN, SSN), OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER));

        String reference = organisationIdClient.initiateAdd(initiateAddOrganisationIdDefaultEmailRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.ORGANISATION_ID_INIT_ADD, RequestTemplate.INIT_ADD_ORGANISATION_ID_TEMPLATE, initiateAddOrganisationIdDefaultEmailRequest, InitiateAddOrganisationIdResponse.class, null);
        Assert.assertEquals(REFERENCE, reference);

        reference = organisationIdClient.initiateAdd(initiateAddOrganisationIdDefaultSsnRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.ORGANISATION_ID_INIT_ADD, RequestTemplate.INIT_ADD_ORGANISATION_ID_TEMPLATE, initiateAddOrganisationIdDefaultSsnRequest, InitiateAddOrganisationIdResponse.class, null);
        Assert.assertEquals(REFERENCE, reference);
    }

    @Test
    public void initAddOrganisationId_customRequests_expectSuccess() throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAddOrganisationIdResponse expectedResponse = new InitiateAddOrganisationIdResponse(REFERENCE);
        OrganisationIdClientApi organisaionIdClient = OrganisationIdClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                .setHttpService(httpServiceMock)
                .build();
        Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class), Mockito.any(RelyingPartyRequest.class), Mockito.eq(InitiateAddOrganisationIdResponse.class), Mockito.anyString())).thenReturn(expectedResponse);

        InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest = InitiateAddOrganisationIdRequest.createCustom()
                .setEmailAndOrganisationId(EMAIL, OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER))
                .setExpiry(expiry)
                .setMinRegistrationLevel(minRegistrationLevel)
                .setRelyingPartyId(RELYING_PARTY_ID)
                .build();

        String reference = organisaionIdClient.initiateAdd(initiateAddOrganisationIdRequest);
        Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.ORGANISATION_ID_INIT_ADD, RequestTemplate.INIT_ADD_ORGANISATION_ID_TEMPLATE, initiateAddOrganisationIdRequest, InitiateAddOrganisationIdResponse.class, RELYING_PARTY_ID);
        Assert.assertEquals(REFERENCE, reference);

    }

    @Test
    public void initAddOrganisationId_userInfoTelefonNumber_expectError() throws FrejaEidClientInternalException, FrejaEidException {
        InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest = InitiateAddOrganisationIdRequest.createCustom()
                .setPhoneNumberAndOrganisationId(EMAIL, OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER))
                .setExpiry(expiry)
                .setMinRegistrationLevel(minRegistrationLevel)
                .setRelyingPartyId(RELYING_PARTY_ID)
                .build();
        try {
            OrganisationIdClientApi organisaionIdClient = OrganisationIdClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                    .setHttpService(httpServiceMock)
                    .build();
            Mockito.when(httpServiceMock.send(Mockito.anyString(), Mockito.any(RequestTemplate.class), Mockito.any(RelyingPartyRequest.class), Mockito.eq(InitiateAddOrganisationIdResponse.class), Mockito.anyString())).thenThrow(new FrejaEidException(FrejaEidErrorCode.INVALID_USER_INFO.getMessage(), FrejaEidErrorCode.INVALID_USER_INFO.getCode()));

            organisaionIdClient.initiateAdd(initiateAddOrganisationIdRequest);
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidException rpEx) {
            Mockito.verify(httpServiceMock).send(FrejaEnvironment.TEST.getUrl() + MethodUrl.ORGANISATION_ID_INIT_ADD, RequestTemplate.INIT_ADD_ORGANISATION_ID_TEMPLATE, initiateAddOrganisationIdRequest, InitiateAddOrganisationIdResponse.class, RELYING_PARTY_ID);
            Assert.assertEquals(1002, rpEx.getErrorCode());
            Assert.assertEquals("Invalid or missing userInfo.", rpEx.getLocalizedMessage());
        }
    }

}
