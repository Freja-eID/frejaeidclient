package com.verisec.frejaeid.client.beans.sign.init;

import com.verisec.frejaeid.client.beans.general.AttributeToReturnInfo;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.enums.*;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.util.UserInfoUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class InitiateSignRequestBuildersTest {

    private static final String EMAIL = "email";
    private static final SsnUserInfo SSN_USER_INFO = SsnUserInfo.create(Country.SWEDEN, "123123123");
    private static final String PHONE_NUMBER = "123123123";
    private static final MinRegistrationLevel REGISTRATION_STATE = MinRegistrationLevel.EXTENDED;
    private static final Long EXPIRY = Long.MIN_VALUE;
    private static final String TITLE = "Title";
    private static final String TEXT = "Text";
    private static final String RELYING_PARTY_ID = "relyingPartyId";
    private static final String ORGANISATION_ID = "orgId";
    private static final String ORG_ID_ISSUER = "orgIdIssuer";
    private static final PushNotification PUSH_NOTIFICATION = PushNotification.create(TITLE, TEXT);
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
        REQUESTED_ATTRIBUTES.add(new AttributeToReturnInfo(AttributeToReturn.DOCUMENT_PHOTO.getName()));
        REQUESTED_ATTRIBUTES.add(new AttributeToReturnInfo(AttributeToReturn.COVID_CERTIFICATES.getName()));
    }

    @Test
    public void createDefaultEmailRequest() {
        InitiateSignRequest expectedInitiateSignRequest =
                new InitiateSignRequest(UserInfoType.EMAIL, EMAIL, MinRegistrationLevel.PLUS, TITLE, null, null,
                                        DataToSignType.SIMPLE_UTF8_TEXT, DataToSign.create(TEXT),
                                        SignatureType.SIMPLE, null, null, null, null);
        InitiateSignRequest initiateSignRequest = InitiateSignRequest.createDefaultWithEmail(EMAIL, TITLE, TEXT);
        Assert.assertEquals(expectedInitiateSignRequest, initiateSignRequest);
    }

    @Test
    public void createDefaultSsnRequest() throws FrejaEidClientInternalException {
        InitiateSignRequest expectedInitiateSignRequest =
                new InitiateSignRequest(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(SSN_USER_INFO),
                                        MinRegistrationLevel.PLUS, TITLE, null, null, DataToSignType.SIMPLE_UTF8_TEXT,
                                        DataToSign.create(TEXT), SignatureType.SIMPLE, null, null, null, null);
        InitiateSignRequest initiateSignRequest = InitiateSignRequest.createDefaultWithSsn(SSN_USER_INFO, TITLE, TEXT);
        Assert.assertEquals(expectedInitiateSignRequest, initiateSignRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypeEmail() {
        InitiateSignRequest expectedInitiateSignRequest =
                new InitiateSignRequest(UserInfoType.EMAIL, EMAIL, REGISTRATION_STATE, TITLE, PUSH_NOTIFICATION,
                                        EXPIRY, DataToSignType.SIMPLE_UTF8_TEXT, DataToSign.create(TEXT),
                                        SignatureType.SIMPLE, REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, ORG_ID_ISSUER,
                                        UserConfirmationMethod.DEFAULT);
        InitiateSignRequest initiateSignRequest = InitiateSignRequest.createCustom()
                .setEmail(EMAIL)
                .setTitle(TITLE)
                .setDataToSign(DataToSign.create(TEXT))
                .setMinRegistrationLevel(REGISTRATION_STATE)
                .setAttributesToReturn(AttributeToReturn.values())
                .setExpiry(EXPIRY)
                .setPushNotification(PUSH_NOTIFICATION)
                .setRelyingPartyId(RELYING_PARTY_ID)
                .setOrgIdIssuer(ORG_ID_ISSUER)
                .setUserConfirmationMethod(UserConfirmationMethod.DEFAULT)
                .build();
        Assert.assertEquals(expectedInitiateSignRequest, initiateSignRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypeEmail_defaultRegistrationState() {
        InitiateSignRequest expectedInitiateSignRequest =
                new InitiateSignRequest(
                        UserInfoType.EMAIL, EMAIL, MinRegistrationLevel.PLUS, TITLE, null, EXPIRY,
                        DataToSignType.SIMPLE_UTF8_TEXT, DataToSign.create(TEXT),
                        SignatureType.SIMPLE, REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, ORG_ID_ISSUER, null);
        InitiateSignRequest initiateSignRequest = InitiateSignRequest.createCustom()
                .setEmail(EMAIL)
                .setTitle(TITLE)
                .setDataToSign(DataToSign.create(TEXT))
                .setAttributesToReturn(AttributeToReturn.values())
                .setExpiry(EXPIRY)
                .setRelyingPartyId(RELYING_PARTY_ID)
                .setOrgIdIssuer(ORG_ID_ISSUER)
                .build();
        Assert.assertEquals(expectedInitiateSignRequest, initiateSignRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypeSsn() throws FrejaEidClientInternalException {
        InitiateSignRequest expectedInitiateSignRequest =
                new InitiateSignRequest(
                        UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(SSN_USER_INFO),
                        REGISTRATION_STATE, TITLE, PUSH_NOTIFICATION, EXPIRY,
                        DataToSignType.SIMPLE_UTF8_TEXT, DataToSign.create(TEXT),
                        SignatureType.SIMPLE, REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, ORG_ID_ISSUER, null);
        InitiateSignRequest initiateSignRequest = InitiateSignRequest.createCustom()
                .setSsn(SSN_USER_INFO)
                .setTitle(TITLE)
                .setDataToSign(DataToSign.create(TEXT))
                .setMinRegistrationLevel(REGISTRATION_STATE)
                .setPushNotification(PUSH_NOTIFICATION)
                .setAttributesToReturn(AttributeToReturn.values())
                .setExpiry(EXPIRY)
                .setRelyingPartyId(RELYING_PARTY_ID)
                .setOrgIdIssuer(ORG_ID_ISSUER)
                .build();
        Assert.assertEquals(expectedInitiateSignRequest, initiateSignRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypePhoneNumber_optionalParamsNull() {
        InitiateSignRequest expectedInitiateSignRequest =
                new InitiateSignRequest(UserInfoType.PHONE, PHONE_NUMBER, MinRegistrationLevel.PLUS, null, null, null,
                                        DataToSignType.SIMPLE_UTF8_TEXT, DataToSign.create(TEXT),
                                        SignatureType.SIMPLE, null, null, null, null);
        InitiateSignRequest initiateSignRequest = InitiateSignRequest.createCustom()
                .setPhoneNumber(PHONE_NUMBER)
                .setTitle(null)
                .setDataToSign(DataToSign.create(TEXT))
                .setMinRegistrationLevel(null)
                .setExpiry(null)
                .setRelyingPartyId(null)
                .build();
        Assert.assertEquals(expectedInitiateSignRequest, initiateSignRequest);
    }

    /**
     * DataToSign tests
     */
    @Test
    public void createCustomRequest_extendedSignatureType() throws FrejaEidClientInternalException {
        InitiateSignRequest expectedInitiateSignRequest =
                new InitiateSignRequest(
                        UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(SSN_USER_INFO),
                        REGISTRATION_STATE, TITLE, PUSH_NOTIFICATION, EXPIRY,
                        DataToSignType.EXTENDED_UTF8_TEXT, DataToSign.create(TEXT, TEXT.getBytes()),
                        SignatureType.EXTENDED, REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, ORG_ID_ISSUER, null);
        InitiateSignRequest initiateSignRequest = InitiateSignRequest.createCustom()
                .setSsn(SSN_USER_INFO)
                .setTitle(TITLE)
                .setDataToSign(DataToSign.create(TEXT, TEXT.getBytes()))
                .setMinRegistrationLevel(REGISTRATION_STATE)
                .setPushNotification(PUSH_NOTIFICATION)
                .setAttributesToReturn(AttributeToReturn.values())
                .setExpiry(EXPIRY)
                .setRelyingPartyId(RELYING_PARTY_ID)
                .setOrgIdIssuer(ORG_ID_ISSUER)
                .build();
        Assert.assertEquals(expectedInitiateSignRequest, initiateSignRequest);
    }

    @Test
    public void createCustomRequest_binaryDataNull() throws FrejaEidClientInternalException {
        InitiateSignRequest expectedInitiateSignRequest =
                new InitiateSignRequest(
                        UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(SSN_USER_INFO),
                        REGISTRATION_STATE, TITLE, PUSH_NOTIFICATION, EXPIRY,
                        DataToSignType.SIMPLE_UTF8_TEXT, DataToSign.create(TEXT),
                        SignatureType.SIMPLE, REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, ORG_ID_ISSUER, null);
        InitiateSignRequest initiateSignRequest = InitiateSignRequest.createCustom()
                .setSsn(SSN_USER_INFO)
                .setTitle(TITLE)
                .setDataToSign(DataToSign.create(TEXT, null))
                .setMinRegistrationLevel(REGISTRATION_STATE)
                .setPushNotification(PUSH_NOTIFICATION)
                .setAttributesToReturn(AttributeToReturn.values())
                .setExpiry(EXPIRY)
                .setRelyingPartyId(RELYING_PARTY_ID)
                .setOrgIdIssuer(ORG_ID_ISSUER)
                .build();
        Assert.assertEquals(expectedInitiateSignRequest, initiateSignRequest);
    }

    @Test
    public void createCustomRequest_binaryDataEmpty() throws FrejaEidClientInternalException {
        InitiateSignRequest expectedInitiateSignRequest =
                new InitiateSignRequest(
                        UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(SSN_USER_INFO),
                        REGISTRATION_STATE, TITLE, PUSH_NOTIFICATION, EXPIRY,
                        DataToSignType.SIMPLE_UTF8_TEXT, DataToSign.create(TEXT, "".getBytes()),
                        SignatureType.SIMPLE, REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, ORG_ID_ISSUER, null);
        InitiateSignRequest initiateSignRequest = InitiateSignRequest.createCustom()
                .setSsn(SSN_USER_INFO)
                .setTitle(TITLE)
                .setDataToSign(DataToSign.create(TEXT, "".getBytes()))
                .setMinRegistrationLevel(REGISTRATION_STATE)
                .setPushNotification(PUSH_NOTIFICATION)
                .setAttributesToReturn(AttributeToReturn.values())
                .setExpiry(EXPIRY)
                .setRelyingPartyId(RELYING_PARTY_ID)
                .setOrgIdIssuer(ORG_ID_ISSUER)
                .build();
        Assert.assertEquals(expectedInitiateSignRequest, initiateSignRequest);
    }

    @Test
    public void createCustomRequest_minRegistrationLevelAndRelyingPartyIdNull() throws FrejaEidClientInternalException {
        InitiateSignRequest expectedInitiateSignRequest =
                new InitiateSignRequest(
                        UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(SSN_USER_INFO),
                        MinRegistrationLevel.PLUS, TITLE, PUSH_NOTIFICATION, EXPIRY,
                        DataToSignType.SIMPLE_UTF8_TEXT, DataToSign.create(TEXT),
                        SignatureType.SIMPLE, REQUESTED_ATTRIBUTES, null, ORG_ID_ISSUER, null);
        InitiateSignRequest initiateSignRequest = InitiateSignRequest.createCustom()
                .setSsn(SSN_USER_INFO)
                .setTitle(TITLE)
                .setDataToSign(DataToSign.create(TEXT))
                .setMinRegistrationLevel(null)
                .setPushNotification(PUSH_NOTIFICATION)
                .setAttributesToReturn(AttributeToReturn.values())
                .setExpiry(EXPIRY)
                .setRelyingPartyId(null)
                .setOrgIdIssuer(ORG_ID_ISSUER)
                .build();
        Assert.assertEquals(expectedInitiateSignRequest, initiateSignRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypeOrganisationId() {
        InitiateSignRequest expectedInitiateSignRequest =
                new InitiateSignRequest(
                        UserInfoType.ORG_ID, ORGANISATION_ID, REGISTRATION_STATE, TITLE,
                        PUSH_NOTIFICATION, EXPIRY, DataToSignType.SIMPLE_UTF8_TEXT,
                        DataToSign.create(TEXT), SignatureType.SIMPLE, REQUESTED_ATTRIBUTES,
                        RELYING_PARTY_ID, ORG_ID_ISSUER, null);
        InitiateSignRequest initiateSignRequest = InitiateSignRequest.createCustom()
                .setOrganisationId(ORGANISATION_ID)
                .setTitle(TITLE)
                .setDataToSign(DataToSign.create(TEXT))
                .setMinRegistrationLevel(REGISTRATION_STATE)
                .setPushNotification(PUSH_NOTIFICATION)
                .setAttributesToReturn(AttributeToReturn.values())
                .setExpiry(EXPIRY)
                .setRelyingPartyId(RELYING_PARTY_ID)
                .setOrgIdIssuer(ORG_ID_ISSUER)
                .build();
        Assert.assertEquals(expectedInitiateSignRequest, initiateSignRequest);
    }

    @Test
    public void createCustomRequest_orgIdIssuerNull() throws FrejaEidClientInternalException {
        InitiateSignRequest expectedInitiateSignRequest =
                new InitiateSignRequest(
                        UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(SSN_USER_INFO),
                        MinRegistrationLevel.PLUS, TITLE, PUSH_NOTIFICATION, EXPIRY,
                        DataToSignType.SIMPLE_UTF8_TEXT, DataToSign.create(TEXT),
                        SignatureType.SIMPLE, REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, null, null);
        InitiateSignRequest initiateSignRequest = InitiateSignRequest.createCustom()
                .setSsn(SSN_USER_INFO)
                .setTitle(TITLE)
                .setDataToSign(DataToSign.create(TEXT))
                .setMinRegistrationLevel(MinRegistrationLevel.PLUS)
                .setPushNotification(PUSH_NOTIFICATION)
                .setAttributesToReturn(AttributeToReturn.values())
                .setExpiry(EXPIRY)
                .setRelyingPartyId(RELYING_PARTY_ID)
                .build();
        Assert.assertEquals(expectedInitiateSignRequest, initiateSignRequest);
    }

    @Test
    public void createCustomRequest_CMSImplicitSignature() throws FrejaEidClientInternalException {
        InitiateSignRequest expectedInitiateSignRequest =
                new InitiateSignRequest(
                        UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(SSN_USER_INFO),
                        MinRegistrationLevel.PLUS, TITLE, null, EXPIRY,
                        DataToSignType.SIMPLE_UTF8_TEXT, DataToSign.create(TEXT),
                        SignatureType.XML_MINAMEDDELANDEN, REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, ORG_ID_ISSUER, null);
        InitiateSignRequest initiateSignRequest = InitiateSignRequest.createCustom()
                .setSsn(SSN_USER_INFO)
                .setTitle(TITLE)
                .setDataToSign(DataToSign.create(TEXT), SignatureType.XML_MINAMEDDELANDEN)
                .setMinRegistrationLevel(MinRegistrationLevel.PLUS)
                .setAttributesToReturn(AttributeToReturn.values())
                .setExpiry(EXPIRY)
                .setRelyingPartyId(RELYING_PARTY_ID)
                .setOrgIdIssuer(ORG_ID_ISSUER)
                .build();
        Assert.assertEquals(expectedInitiateSignRequest, initiateSignRequest);
    }

    @Test
    public void createCustomRequest_userConfirmationMethodSet() {
        InitiateSignRequest expectedInitiateSignRequest =
                new InitiateSignRequest(UserInfoType.EMAIL, EMAIL, REGISTRATION_STATE, TITLE, PUSH_NOTIFICATION,
                                        EXPIRY, DataToSignType.SIMPLE_UTF8_TEXT, DataToSign.create(TEXT),
                                        SignatureType.SIMPLE, REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, ORG_ID_ISSUER,
                                        UserConfirmationMethod.DEFAULT_AND_FACE);
        InitiateSignRequest initiateSignRequest = InitiateSignRequest.createCustom()
                .setEmail(EMAIL)
                .setTitle(TITLE)
                .setDataToSign(DataToSign.create(TEXT))
                .setMinRegistrationLevel(REGISTRATION_STATE)
                .setAttributesToReturn(AttributeToReturn.values())
                .setExpiry(EXPIRY)
                .setPushNotification(PUSH_NOTIFICATION)
                .setRelyingPartyId(RELYING_PARTY_ID)
                .setOrgIdIssuer(ORG_ID_ISSUER)
                .setUserConfirmationMethod(UserConfirmationMethod.DEFAULT_AND_FACE)
                .build();
        Assert.assertEquals(expectedInitiateSignRequest, initiateSignRequest);
    }
}
