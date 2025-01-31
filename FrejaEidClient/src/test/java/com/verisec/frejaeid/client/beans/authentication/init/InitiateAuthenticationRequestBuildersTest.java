package com.verisec.frejaeid.client.beans.authentication.init;

import com.verisec.frejaeid.client.beans.general.AttributeToReturnInfo;
import com.verisec.frejaeid.client.beans.general.OriginDeviceDetails;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.enums.*;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.util.UserInfoUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class InitiateAuthenticationRequestBuildersTest {

    private static final String EMAIL = "email";
    private static final SsnUserInfo SSN_USER_INFO = SsnUserInfo.create(Country.SWEDEN, "123123123");
    private static final String PHONE_NUMBER = "123123123";
    private static final String INFERRED_USER_INFO = "N/A";
    private static final MinRegistrationLevel REGISTRATION_STATE = MinRegistrationLevel.EXTENDED;
    private static final String RELYING_PARTY_ID = "relyingPartyId";
    private static final String ORGANISATION_ID = "orgId";
    private static final String ORG_ID_ISSUER = "orgIdIssuer";
    private static final OriginDeviceDetails ORIGIN_DEVICE_DETAILS = OriginDeviceDetails.create("frejaCookie");
    private static final Set<AttributeToReturnInfo> REQUESTED_ATTRIBUTES = new HashSet<>();

    @BeforeClass
    public static void createRequestedAttributes() {
        REQUESTED_ATTRIBUTES.add(attributeOf(AttributeToReturn.SSN));
        REQUESTED_ATTRIBUTES.add(attributeOf(AttributeToReturn.BASIC_USER_INFO));
        REQUESTED_ATTRIBUTES.add(attributeOf(AttributeToReturn.CUSTOM_IDENTIFIER));
        REQUESTED_ATTRIBUTES.add(attributeOf(AttributeToReturn.DATE_OF_BIRTH));
        REQUESTED_ATTRIBUTES.add(attributeOf(AttributeToReturn.EMAIL_ADDRESS));
        REQUESTED_ATTRIBUTES.add(attributeOf(AttributeToReturn.INTEGRATOR_SPECIFIC_USER_ID));
        REQUESTED_ATTRIBUTES.add(attributeOf(AttributeToReturn.RELYING_PARTY_USER_ID));
        REQUESTED_ATTRIBUTES.add(attributeOf(AttributeToReturn.ORGANISATION_ID_IDENTIFIER));
        REQUESTED_ATTRIBUTES.add(attributeOf(AttributeToReturn.ORGANISATION_ID));
        REQUESTED_ATTRIBUTES.add(attributeOf(AttributeToReturn.ADDRESSES));
        REQUESTED_ATTRIBUTES.add(attributeOf(AttributeToReturn.ALL_EMAIL_ADDRESSES));
        REQUESTED_ATTRIBUTES.add(attributeOf(AttributeToReturn.ALL_PHONE_NUMBERS));
        REQUESTED_ATTRIBUTES.add(attributeOf(AttributeToReturn.REGISTRATION_LEVEL));
        REQUESTED_ATTRIBUTES.add(attributeOf(AttributeToReturn.AGE));
        REQUESTED_ATTRIBUTES.add(attributeOf(AttributeToReturn.PHOTO));
        REQUESTED_ATTRIBUTES.add(attributeOf(AttributeToReturn.DOCUMENT));
        REQUESTED_ATTRIBUTES.add(attributeOf(AttributeToReturn.DOCUMENT_PHOTO));
        REQUESTED_ATTRIBUTES.add(attributeOf(AttributeToReturn.COVID_CERTIFICATES));
        REQUESTED_ATTRIBUTES.add(attributeOf(AttributeToReturn.DOCUMENT_INFO_WITH_PDF));
        REQUESTED_ATTRIBUTES.add(attributeOf(AttributeToReturn.CHILDREN_DOCUMENT_INFO_WITH_PDF));
        REQUESTED_ATTRIBUTES.add(attributeOf(AttributeToReturn.NETWORK_INFO));
        REQUESTED_ATTRIBUTES.add(attributeOf(AttributeToReturn.LOA_LEVEL));
        REQUESTED_ATTRIBUTES.add(attributeOf(AttributeToReturn.UNIQUE_PERSONAL_IDENTIFIER));
    }

    private static AttributeToReturnInfo attributeOf(AttributeToReturn attributeToReturn){
        return new AttributeToReturnInfo(attributeToReturn.getName());
    }

    @Test
    public void createDefaultEmailRequest() {
        InitiateAuthenticationRequest expectedInitiateAuthenticationRequest =
                new InitiateAuthenticationRequest(
                        UserInfoType.EMAIL, EMAIL, MinRegistrationLevel.BASIC, null, null, null, null, null);
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createDefaultWithEmail(EMAIL);
        Assert.assertEquals(expectedInitiateAuthenticationRequest, initiateAuthenticationRequest);
    }

    @Test
    public void createDefaultSsnRequest() throws FrejaEidClientInternalException {
        InitiateAuthenticationRequest expectedInitiateAuthenticationRequest =
                new InitiateAuthenticationRequest(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(SSN_USER_INFO),
                                                  MinRegistrationLevel.BASIC, null, null, null, null, null);
        InitiateAuthenticationRequest initiateAuthenticationRequest =
                InitiateAuthenticationRequest.createDefaultWithSsn(SSN_USER_INFO);
        Assert.assertEquals(expectedInitiateAuthenticationRequest, initiateAuthenticationRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypeEmail() {
        InitiateAuthenticationRequest expectedInitiateAuthenticationRequest =
                new InitiateAuthenticationRequest(UserInfoType.EMAIL, EMAIL, MinRegistrationLevel.EXTENDED,
                                                  REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, ORG_ID_ISSUER, null, null);
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
                                                  REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, ORG_ID_ISSUER, null, null);
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
                                                  RELYING_PARTY_ID, ORG_ID_ISSUER, null, null);
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
                                                  REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, ORG_ID_ISSUER, null, null);
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
                                                  REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, ORG_ID_ISSUER,
                                                  UserConfirmationMethod.DEFAULT, ORIGIN_DEVICE_DETAILS);
        InitiateAuthenticationRequest initiateAuthenticationRequest = InitiateAuthenticationRequest.createCustom()
                .setInferred()
                .setAttributesToReturn(AttributeToReturn.values())
                .setMinRegistrationLevel(REGISTRATION_STATE)
                .setRelyingPartyId(RELYING_PARTY_ID)
                .setOrgIdIssuer(ORG_ID_ISSUER)
                .setUserConfirmationMethod(UserConfirmationMethod.DEFAULT)
                .setOriginDeviceDetails(ORIGIN_DEVICE_DETAILS)
                .build();
        Assert.assertEquals(expectedInitiateAuthenticationRequest, initiateAuthenticationRequest);
    }

    @Test
    public void createCustomRequest_minRegistrationLevelAndRelyingPartyIdNull() {
        InitiateAuthenticationRequest expectedInitiateAuthenticationRequest =
                new InitiateAuthenticationRequest(UserInfoType.INFERRED, INFERRED_USER_INFO,
                                                  MinRegistrationLevel.BASIC, REQUESTED_ATTRIBUTES, null,
                                                  ORG_ID_ISSUER, null, null);
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
                                                  REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, ORG_ID_ISSUER, null, null);
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
                                                  null, null, null);
        InitiateAuthenticationRequest initiateAuthenticationRequest = InitiateAuthenticationRequest.createCustom()
                .setInferred()
                .setAttributesToReturn(AttributeToReturn.values())
                .setMinRegistrationLevel(null)
                .setRelyingPartyId(null)
                .setOrgIdIssuer(null)
                .build();
        Assert.assertEquals(expectedInitiateAuthenticationRequest, initiateAuthenticationRequest);
    }

    @Test
    public void createCustomRequest_userConfirmationMethodSet() {
        InitiateAuthenticationRequest expectedInitiateAuthenticationRequest =
                new InitiateAuthenticationRequest(UserInfoType.EMAIL, EMAIL, MinRegistrationLevel.EXTENDED,
                                                  REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, ORG_ID_ISSUER,
                                                  UserConfirmationMethod.DEFAULT_AND_FACE, ORIGIN_DEVICE_DETAILS);
        InitiateAuthenticationRequest initiateAuthenticationRequest = InitiateAuthenticationRequest.createCustom()
                .setEmail(EMAIL)
                .setMinRegistrationLevel(REGISTRATION_STATE)
                .setAttributesToReturn(AttributeToReturn.values())
                .setRelyingPartyId(RELYING_PARTY_ID)
                .setOrgIdIssuer(ORG_ID_ISSUER)
                .setUserConfirmationMethod(UserConfirmationMethod.DEFAULT_AND_FACE)
                .setOriginDeviceDetails(ORIGIN_DEVICE_DETAILS)
                .build();
        Assert.assertEquals(expectedInitiateAuthenticationRequest, initiateAuthenticationRequest);
    }
    
        @Test
    public void createCustomRequest_originDeviceDetailsSet() {
        InitiateAuthenticationRequest expectedInitiateAuthenticationRequest =
                new InitiateAuthenticationRequest(UserInfoType.EMAIL, EMAIL, MinRegistrationLevel.EXTENDED,
                                                  REQUESTED_ATTRIBUTES, null, null,
                                                  null, ORIGIN_DEVICE_DETAILS);
        InitiateAuthenticationRequest initiateAuthenticationRequest = InitiateAuthenticationRequest.createCustom()
                .setEmail(EMAIL)
                .setMinRegistrationLevel(REGISTRATION_STATE)
                .setAttributesToReturn(AttributeToReturn.values())
                .setOriginDeviceDetails(ORIGIN_DEVICE_DETAILS)
                .build();
        Assert.assertEquals(expectedInitiateAuthenticationRequest, initiateAuthenticationRequest);
    }
}
