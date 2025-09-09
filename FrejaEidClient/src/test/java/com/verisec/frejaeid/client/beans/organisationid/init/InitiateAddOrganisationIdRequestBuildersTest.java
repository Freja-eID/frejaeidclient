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
    private static final SsnUserInfo SSN_USER_INFO = SsnUserInfo.create(Country.SWEDEN, "123123123");
    private static final String PHONE_NUMBER = "123123123";
    private static final MinRegistrationLevel REGISTRATION_STATE = MinRegistrationLevel.PLUS;
    private static final Long EXPIRY = Long.MIN_VALUE;
    private static final String ORGANISATION_ID_TITLE = "OrgananisationId title";
    private static final String IDENTIFIER_NAME = "Identifier name";
    private static final String IDENTIFIER = "identifier";
    private static final String RELYING_PARTY_ID = "relyingPartyId";
    private static final String INFERRED_USER_INFO = "N/A";
    private static final OrganisationId ORGANISATION_ID =
            OrganisationId.create(ORGANISATION_ID_TITLE, IDENTIFIER_NAME, IDENTIFIER);

    @Test
    public void createDefaultEmailRequest() {
        InitiateAddOrganisationIdRequest expectedInitiateAddOrganisationIdRequest =
                new InitiateAddOrganisationIdRequest(UserInfoType.EMAIL, EMAIL, ORGANISATION_ID,
                                                     MinRegistrationLevel.EXTENDED, null, null, false);
        InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest =
                InitiateAddOrganisationIdRequest.createDefaultWithEmail(EMAIL, ORGANISATION_ID);
        Assert.assertEquals(expectedInitiateAddOrganisationIdRequest, initiateAddOrganisationIdRequest);
    }

    @Test
    public void createDefaultSsnRequest() throws FrejaEidClientInternalException {
        InitiateAddOrganisationIdRequest expectedInitiateAddOrganisationIdRequest =
                new InitiateAddOrganisationIdRequest(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(SSN_USER_INFO),
                                                     ORGANISATION_ID, MinRegistrationLevel.EXTENDED, null, null, false);
        InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest =
                InitiateAddOrganisationIdRequest.createDefaultWithSsn(SSN_USER_INFO, ORGANISATION_ID);
        Assert.assertEquals(expectedInitiateAddOrganisationIdRequest, initiateAddOrganisationIdRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypeEmail() {
        InitiateAddOrganisationIdRequest expectedInitiateAddOrganisationIdRequest =
                new InitiateAddOrganisationIdRequest(UserInfoType.EMAIL, EMAIL, ORGANISATION_ID,
                                                     REGISTRATION_STATE, EXPIRY, RELYING_PARTY_ID, false);
        InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest =
                InitiateAddOrganisationIdRequest.createCustom()
                        .setEmailAndOrganisationId(EMAIL, ORGANISATION_ID)
                        .setMinRegistrationLevel(REGISTRATION_STATE)
                        .setExpiry(EXPIRY)
                        .setRelyingPartyId(RELYING_PARTY_ID)
                        .build();
        Assert.assertEquals(expectedInitiateAddOrganisationIdRequest, initiateAddOrganisationIdRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypeEmail_defaultRegistrationState() {
        InitiateAddOrganisationIdRequest expectedInitiateAddOrganisationIdRequest =
                new InitiateAddOrganisationIdRequest(UserInfoType.EMAIL, EMAIL, ORGANISATION_ID,
                                                     MinRegistrationLevel.EXTENDED, null, RELYING_PARTY_ID, false);
        InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest =
                InitiateAddOrganisationIdRequest.createCustom()
                        .setEmailAndOrganisationId(EMAIL, ORGANISATION_ID)
                        .setRelyingPartyId(RELYING_PARTY_ID)
                        .build();
        Assert.assertEquals(expectedInitiateAddOrganisationIdRequest, initiateAddOrganisationIdRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypeSsn() throws FrejaEidClientInternalException {
        InitiateAddOrganisationIdRequest expectedInitiateAddOrganisationIdRequest =
                new InitiateAddOrganisationIdRequest(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(SSN_USER_INFO),
                                                     ORGANISATION_ID, MinRegistrationLevel.EXTENDED, null,
                                                     RELYING_PARTY_ID, false);
        InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest =
                InitiateAddOrganisationIdRequest.createCustom()
                        .setSsnAndOrganisationId(SSN_USER_INFO, ORGANISATION_ID)
                        .setRelyingPartyId(RELYING_PARTY_ID)
                        .build();
        Assert.assertEquals(expectedInitiateAddOrganisationIdRequest, initiateAddOrganisationIdRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypePhoneNumber() {
        InitiateAddOrganisationIdRequest expectedInitiateAddOrganisationIdRequest =
                new InitiateAddOrganisationIdRequest(UserInfoType.PHONE, PHONE_NUMBER, ORGANISATION_ID,
                                                     MinRegistrationLevel.EXTENDED, EXPIRY, null, false);
        InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest =
                InitiateAddOrganisationIdRequest.createCustom()
                        .setPhoneNumberAndOrganisationId(PHONE_NUMBER, ORGANISATION_ID)
                        .setExpiry(EXPIRY)
                        .build();
        Assert.assertEquals(expectedInitiateAddOrganisationIdRequest, initiateAddOrganisationIdRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypeInferred() {
        InitiateAddOrganisationIdRequest expectedInitiateAuthenticationRequest =
                new InitiateAddOrganisationIdRequest(UserInfoType.INFERRED, INFERRED_USER_INFO, ORGANISATION_ID,
                                                     MinRegistrationLevel.EXTENDED, EXPIRY, null, false);
        InitiateAddOrganisationIdRequest initiateAuthenticationRequest =
                InitiateAddOrganisationIdRequest.createCustom()
                        .setInferredAndOrganisationId(ORGANISATION_ID)
                        .setExpiry(EXPIRY)
                        .build();
        Assert.assertEquals(expectedInitiateAuthenticationRequest, initiateAuthenticationRequest);
    }

    @Test
    public void createCustomRequest_minRegistrationLevelAndRelyingPartyIdNull() {
        InitiateAddOrganisationIdRequest expectedInitiateAddOrganisationIdRequest =
                new InitiateAddOrganisationIdRequest(UserInfoType.PHONE, PHONE_NUMBER, ORGANISATION_ID,
                                                     MinRegistrationLevel.EXTENDED, EXPIRY, null, false);
        InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest =
                InitiateAddOrganisationIdRequest.createCustom()
                        .setPhoneNumberAndOrganisationId(PHONE_NUMBER, ORGANISATION_ID)
                        .setExpiry(EXPIRY)
                        .setMinRegistrationLevel(null)
                        .setRelyingPartyId(null)
                        .build();
        Assert.assertEquals(expectedInitiateAddOrganisationIdRequest, initiateAddOrganisationIdRequest);
    }

    @Test
    public void createCustomRequest_userDynamicQrCodeSet() {
        InitiateAddOrganisationIdRequest expectedInitiateAddOrganisationIdRequest =
                new InitiateAddOrganisationIdRequest(UserInfoType.EMAIL, EMAIL, ORGANISATION_ID,
                                                     REGISTRATION_STATE, EXPIRY, RELYING_PARTY_ID, true);
        InitiateAddOrganisationIdRequest initiateAddOrganisationIdRequest =
                InitiateAddOrganisationIdRequest.createCustom()
                        .setEmailAndOrganisationId(EMAIL, ORGANISATION_ID)
                        .setMinRegistrationLevel(REGISTRATION_STATE)
                        .setExpiry(EXPIRY)
                        .setRelyingPartyId(RELYING_PARTY_ID)
                        .setUseDynamicQrCode(true)
                        .build();
        Assert.assertEquals(expectedInitiateAddOrganisationIdRequest, initiateAddOrganisationIdRequest);
    }
}
