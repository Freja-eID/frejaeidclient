package com.verisec.frejaeid.client.beans.organisationid.init;

import com.verisec.frejaeid.client.beans.general.OrganisationId;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.enums.Country;
import com.verisec.frejaeid.client.enums.MinRegistrationLevel;
import com.verisec.frejaeid.client.enums.UserInfoType;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.util.UserInfoUtil;
import org.junit.Assert;
import org.junit.Test;

public class InitiateAddOrganisationIdRequestBuildersTest {

    private static final String EMAIL = "email";
    private static final SsnUserInfo SSN_USER_INFO = new SsnUserInfo(Country.SWEDEN, "123123123");
    private static final String PHONE_NUMBER = "123123123";
    private static final MinRegistrationLevel REGISTRATION_STATE = MinRegistrationLevel.PLUS;
    private static final Long EXPIRY = Long.MIN_VALUE;
    private static final String ORGANISATION_ID_TITLE = "OrgananisationId title";
    private static final String IDENTIFIER_NAME = "Identifier name";
    private static final String IDENTIFIER = "identifier";
    private static final String RELYING_PARTY_ID = "relyingPartyId";
    private static final String INFERRED_USER_INFO = "N/A";

    @Test
    public void createDefaultEmailRequest() {
        InitiateAddOrganisationIdRequest expectedInitiateAddOrganisationIdRequest = new InitiateAddOrganisationIdRequest(UserInfoType.EMAIL, EMAIL, OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER), MinRegistrationLevel.EXTENDED, null, null);
        InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest = InitiateAddOrganisationIdRequest.createDefaultWithEmail(EMAIL, OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER));
        Assert.assertEquals(expectedInitiateAddOrganisationIdRequest, initiateAddOrganisationIdRequest);
    }

    @Test
    public void createDefaultSsnRequest() throws FrejaEidClientInternalException {
        InitiateAddOrganisationIdRequest expectedInitiateAddOrganisationIdRequest = new InitiateAddOrganisationIdRequest(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(SSN_USER_INFO), OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER), MinRegistrationLevel.EXTENDED, null, null);
        InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest = InitiateAddOrganisationIdRequest.createDefaultWithSsn(SSN_USER_INFO, OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER));
        Assert.assertEquals(expectedInitiateAddOrganisationIdRequest, initiateAddOrganisationIdRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypeEmail() {
        InitiateAddOrganisationIdRequest expectedInitiateAddOrganisationIdRequest = new InitiateAddOrganisationIdRequest(UserInfoType.EMAIL, EMAIL, OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER), REGISTRATION_STATE, EXPIRY, RELYING_PARTY_ID);
        InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest = InitiateAddOrganisationIdRequest.createCustom()
                .setEmailAndOrganisationId(EMAIL, OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER))
                .setMinRegistrationLevel(REGISTRATION_STATE)
                .setExpiry(EXPIRY)
                .setRelyingPartyId(RELYING_PARTY_ID)
                .build();
        Assert.assertEquals(expectedInitiateAddOrganisationIdRequest, initiateAddOrganisationIdRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypeEmail_defaultRegistrationState() {
        InitiateAddOrganisationIdRequest expectedInitiateAddOrganisationIdRequest = new InitiateAddOrganisationIdRequest(UserInfoType.EMAIL, EMAIL, OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER), MinRegistrationLevel.EXTENDED, null, RELYING_PARTY_ID);
        InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest = InitiateAddOrganisationIdRequest.createCustom()
                .setEmailAndOrganisationId(EMAIL, OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER))
                .setRelyingPartyId(RELYING_PARTY_ID)
                .build();
        Assert.assertEquals(expectedInitiateAddOrganisationIdRequest, initiateAddOrganisationIdRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypeSsn() throws FrejaEidClientInternalException {
        InitiateAddOrganisationIdRequest expectedInitiateAddOrganisationIdRequest = new InitiateAddOrganisationIdRequest(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(SSN_USER_INFO), OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER), MinRegistrationLevel.EXTENDED, null, RELYING_PARTY_ID);
        InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest = InitiateAddOrganisationIdRequest.createCustom()
                .setSsnAndOrganisationId(SSN_USER_INFO, OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER))
                .setRelyingPartyId(RELYING_PARTY_ID)
                .build();
        Assert.assertEquals(expectedInitiateAddOrganisationIdRequest, initiateAddOrganisationIdRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypePhoneNumber() {
        InitiateAddOrganisationIdRequest expectedInitiateAddOrganisationIdRequest = new InitiateAddOrganisationIdRequest(UserInfoType.PHONE, PHONE_NUMBER, OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER), MinRegistrationLevel.EXTENDED, EXPIRY, null);
        InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest = InitiateAddOrganisationIdRequest.createCustom()
                .setPhoneNumberAndOrganisationId(PHONE_NUMBER, OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER))
                .setExpiry(EXPIRY)
                .build();
        Assert.assertEquals(expectedInitiateAddOrganisationIdRequest, initiateAddOrganisationIdRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypeInferred() {
        InitiateAddOrganisationIdRequest expectedInitiateAuthenticationRequest = new InitiateAddOrganisationIdRequest(UserInfoType.INFERRED, INFERRED_USER_INFO, OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER), MinRegistrationLevel.EXTENDED, EXPIRY, null);
        InitiateAddOrganisationIdRequest initiateAuthenticationRequest = InitiateAddOrganisationIdRequest.createCustom()
                .setInferredAndOrganisationId(OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER))
                .setExpiry(EXPIRY)
                .build();
        Assert.assertEquals(expectedInitiateAuthenticationRequest, initiateAuthenticationRequest);
    }

    @Test
    public void createCustomRequest_minRegistrationLevelAndRelyingPartyIdNull() {
        InitiateAddOrganisationIdRequest expectedInitiateAddOrganisationIdRequest = new InitiateAddOrganisationIdRequest(UserInfoType.PHONE, PHONE_NUMBER, OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER), MinRegistrationLevel.EXTENDED, EXPIRY, null);
        InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest = InitiateAddOrganisationIdRequest.createCustom()
                .setPhoneNumberAndOrganisationId(PHONE_NUMBER, OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER))
                .setExpiry(EXPIRY)
                .setMinRegistrationLevel(null)
                .setRelyingPartyId(null)
                .build();
        Assert.assertEquals(expectedInitiateAddOrganisationIdRequest, initiateAddOrganisationIdRequest);
    }

}
