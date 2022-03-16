package com.verisec.frejaeid.client.beans.authentication.init;

import com.verisec.frejaeid.client.beans.general.AttributeToReturnInfo;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.enums.AttributeToReturn;
import com.verisec.frejaeid.client.enums.Country;
import com.verisec.frejaeid.client.enums.MinRegistrationLevel;
import com.verisec.frejaeid.client.enums.UserInfoType;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.util.UserInfoUtil;
import java.util.HashSet;

import java.util.Set;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class InitiateAuthenticationRequestBuildersTest {

    private static final String EMAIL = "email";
    private static final SsnUserInfo SSN_USER_INFO = SsnUserInfo.create(Country.SWEDEN, "123123123");
    private static final String PHONE_NUMBER = "123123123";
    private static final String INFERRED_USER_INFO = "N/A";
    private static final MinRegistrationLevel REGISTRATION_STATE = MinRegistrationLevel.EXTENDED;
    private static final String RELYING_PARTY_ID = "relyingPartyId";
    private static final String ORGANISATION_ID = "orgId";
    private static final String ORG_ID_ISSUER = "orgIdIssuer";
    private static final Set<AttributeToReturnInfo> REQUESTED_ATTRIBUTES = new HashSet<>();

    @BeforeClass
    public static void createRequestedAttributes() {
        REQUESTED_ATTRIBUTES.add(new AttributeToReturnInfo(AttributeToReturn.SSN.getName()));
        REQUESTED_ATTRIBUTES.add(new AttributeToReturnInfo(AttributeToReturn.BASIC_USER_INFO.getName()));
        REQUESTED_ATTRIBUTES.add(new AttributeToReturnInfo(AttributeToReturn.CUSTOM_IDENTIFIER.getName()));
        REQUESTED_ATTRIBUTES.add(new AttributeToReturnInfo(AttributeToReturn.DATE_OF_BIRTH.getName()));
        REQUESTED_ATTRIBUTES.add(new AttributeToReturnInfo(AttributeToReturn.EMAIL_ADDRESS.getName()));
        REQUESTED_ATTRIBUTES.add(new AttributeToReturnInfo(AttributeToReturn.INTEGRATOR_SPECIFIC_USER_ID.getName()));
        REQUESTED_ATTRIBUTES.add(new AttributeToReturnInfo(AttributeToReturn.RELYING_PARTY_USER_ID.getName()));
        REQUESTED_ATTRIBUTES.add(new AttributeToReturnInfo(AttributeToReturn.ORGANISATION_ID_IDENTIFIER.getName()));
        REQUESTED_ATTRIBUTES.add(new AttributeToReturnInfo(AttributeToReturn.ORGANISATION_ID.getName()));
        REQUESTED_ATTRIBUTES.add(new AttributeToReturnInfo(AttributeToReturn.ADDRESSES.getName()));
        REQUESTED_ATTRIBUTES.add(new AttributeToReturnInfo(AttributeToReturn.ALL_EMAIL_ADDRESSES.getName()));
        REQUESTED_ATTRIBUTES.add(new AttributeToReturnInfo(AttributeToReturn.ALL_PHONE_NUMBERS.getName()));
        REQUESTED_ATTRIBUTES.add(new AttributeToReturnInfo(AttributeToReturn.REGISTRATION_LEVEL.getName()));
        REQUESTED_ATTRIBUTES.add(new AttributeToReturnInfo(AttributeToReturn.AGE.getName()));
        REQUESTED_ATTRIBUTES.add(new AttributeToReturnInfo(AttributeToReturn.PHOTO.getName()));
        REQUESTED_ATTRIBUTES.add(new AttributeToReturnInfo(AttributeToReturn.DOCUMENT.getName()));
        REQUESTED_ATTRIBUTES.add(new AttributeToReturnInfo(AttributeToReturn.COVID_CERTIFICATES.getName()));
    }

    @Test
    public void createDefaultEmailRequest() {
        InitiateAuthenticationRequest expectedInitiateAuthenticationRequest =
                new InitiateAuthenticationRequest(UserInfoType.EMAIL, EMAIL, MinRegistrationLevel.BASIC, null, null, null);
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createDefaultWithEmail(EMAIL);
        Assert.assertEquals(expectedInitiateAuthenticationRequest, initiateAuthenticationRequest);
    }

    @Test
    public void createDefaultSsnRequest() throws FrejaEidClientInternalException {
        InitiateAuthenticationRequest expectedInitiateAuthenticationRequest =
                new InitiateAuthenticationRequest(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(SSN_USER_INFO),
                                                  MinRegistrationLevel.BASIC, null, null, null);
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createDefaultWithSsn(SSN_USER_INFO);
        Assert.assertEquals(expectedInitiateAuthenticationRequest, initiateAuthenticationRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypeEmail() {
        InitiateAuthenticationRequest expectedInitiateAuthenticationRequest =
                new InitiateAuthenticationRequest(UserInfoType.EMAIL, EMAIL, MinRegistrationLevel.EXTENDED,
                                                  REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, ORG_ID_ISSUER);
        InitiateAuthenticationRequest initiateAuthenticationRequest = InitiateAuthenticationRequest.createCustom()
                .setEmail(EMAIL)
                .setMinRegistrationLevel(REGISTRATION_STATE)
                .setAttributesToReturn(AttributeToReturn.values())
                .setRelyingPartyId(RELYING_PARTY_ID)
                .setOrgIdIssuer(ORG_ID_ISSUER)
                .build();
        Assert.assertEquals(expectedInitiateAuthenticationRequest, initiateAuthenticationRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypeEmail_defaultRegistrationState() {
        InitiateAuthenticationRequest expectedInitiateAuthenticationRequest =
                new InitiateAuthenticationRequest(UserInfoType.EMAIL, EMAIL, MinRegistrationLevel.BASIC,
                                                  REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, ORG_ID_ISSUER);
        InitiateAuthenticationRequest initiateAuthenticationRequest = InitiateAuthenticationRequest.createCustom()
                .setEmail(EMAIL)
                .setAttributesToReturn(AttributeToReturn.values())
                .setRelyingPartyId(RELYING_PARTY_ID)
                .setOrgIdIssuer(ORG_ID_ISSUER)
                .build();
        Assert.assertEquals(expectedInitiateAuthenticationRequest, initiateAuthenticationRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypeSsn() throws FrejaEidClientInternalException {
        InitiateAuthenticationRequest expectedInitiateAuthenticationRequest =
                new InitiateAuthenticationRequest(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(SSN_USER_INFO),
                                                  MinRegistrationLevel.EXTENDED, REQUESTED_ATTRIBUTES,
                                                  RELYING_PARTY_ID, ORG_ID_ISSUER);
        InitiateAuthenticationRequest initiateAuthenticationRequest = InitiateAuthenticationRequest.createCustom()
                .setSsn(SSN_USER_INFO)
                .setAttributesToReturn(AttributeToReturn.values())
                .setMinRegistrationLevel(REGISTRATION_STATE)
                .setRelyingPartyId(RELYING_PARTY_ID)
                .setOrgIdIssuer(ORG_ID_ISSUER)
                .build();
        Assert.assertEquals(expectedInitiateAuthenticationRequest, initiateAuthenticationRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypePhoneNumber() {
        InitiateAuthenticationRequest expectedInitiateAuthenticationRequest =
                new InitiateAuthenticationRequest(UserInfoType.PHONE, PHONE_NUMBER, REGISTRATION_STATE,
                                                  REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, ORG_ID_ISSUER);
        InitiateAuthenticationRequest initiateAuthenticationRequest = InitiateAuthenticationRequest.createCustom()
                .setPhoneNumber(PHONE_NUMBER)
                .setAttributesToReturn(AttributeToReturn.values())
                .setMinRegistrationLevel(REGISTRATION_STATE)
                .setRelyingPartyId(RELYING_PARTY_ID)
                .setOrgIdIssuer(ORG_ID_ISSUER)
                .build();
        Assert.assertEquals(expectedInitiateAuthenticationRequest, initiateAuthenticationRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypeInferred() {
        InitiateAuthenticationRequest expectedInitiateAuthenticationRequest =
                new InitiateAuthenticationRequest(UserInfoType.INFERRED, INFERRED_USER_INFO, REGISTRATION_STATE,
                                                  REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, ORG_ID_ISSUER);
        InitiateAuthenticationRequest initiateAuthenticationRequest = InitiateAuthenticationRequest.createCustom()
                .setInferred()
                .setAttributesToReturn(AttributeToReturn.values())
                .setMinRegistrationLevel(REGISTRATION_STATE)
                .setRelyingPartyId(RELYING_PARTY_ID)
                .setOrgIdIssuer(ORG_ID_ISSUER)
                .build();
        Assert.assertEquals(expectedInitiateAuthenticationRequest, initiateAuthenticationRequest);
    }

    @Test
    public void createCustomRequest_minRegistrationLevelAndRelyingPartyIdNull() {
        InitiateAuthenticationRequest expectedInitiateAuthenticationRequest =
                new InitiateAuthenticationRequest(UserInfoType.INFERRED, INFERRED_USER_INFO,
                                                  MinRegistrationLevel.BASIC, REQUESTED_ATTRIBUTES, null,
                                                  ORG_ID_ISSUER);
        InitiateAuthenticationRequest initiateAuthenticationRequest = InitiateAuthenticationRequest.createCustom()
                .setInferred()
                .setAttributesToReturn(AttributeToReturn.values())
                .setMinRegistrationLevel(null)
                .setRelyingPartyId(null)
                .setOrgIdIssuer(ORG_ID_ISSUER)
                .build();
        Assert.assertEquals(expectedInitiateAuthenticationRequest, initiateAuthenticationRequest);
    }

    @Test
    public void createCustomRequest_userInfoOrganisationId() {
        InitiateAuthenticationRequest expectedInitiateAuthenticationRequest =
                new InitiateAuthenticationRequest(UserInfoType.ORG_ID, ORGANISATION_ID, MinRegistrationLevel.EXTENDED,
                                                  REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, ORG_ID_ISSUER);
        InitiateAuthenticationRequest initiateAuthenticationRequest = InitiateAuthenticationRequest.createCustom()
                .setOrganisationId(ORGANISATION_ID)
                .setAttributesToReturn(AttributeToReturn.values())
                .setMinRegistrationLevel(REGISTRATION_STATE)
                .setRelyingPartyId(RELYING_PARTY_ID)
                .setOrgIdIssuer(ORG_ID_ISSUER)
                .build();
        Assert.assertEquals(expectedInitiateAuthenticationRequest, initiateAuthenticationRequest);
    }

    @Test
    public void createCustomRequest_orgIdIssuerNull() {
        InitiateAuthenticationRequest expectedInitiateAuthenticationRequest =
                new InitiateAuthenticationRequest(UserInfoType.INFERRED, INFERRED_USER_INFO,
                                                  MinRegistrationLevel.BASIC, REQUESTED_ATTRIBUTES, null,
                                                  null);
        InitiateAuthenticationRequest initiateAuthenticationRequest = InitiateAuthenticationRequest.createCustom()
                .setInferred()
                .setAttributesToReturn(AttributeToReturn.values())
                .setMinRegistrationLevel(null)
                .setRelyingPartyId(null)
                .setOrgIdIssuer(null)
                .build();
        Assert.assertEquals(expectedInitiateAuthenticationRequest, initiateAuthenticationRequest);
    }
}
