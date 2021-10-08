package com.verisec.frejaeid.client.beans.sign.init;

import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.enums.AttributeToReturn;
import com.verisec.frejaeid.client.enums.Country;
import com.verisec.frejaeid.client.enums.DataToSignType;
import com.verisec.frejaeid.client.enums.MinRegistrationLevel;
import com.verisec.frejaeid.client.enums.SignatureType;
import com.verisec.frejaeid.client.enums.UserInfoType;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.util.UserInfoUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Set;
import java.util.TreeSet;

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
    private static final Set<AttributeToReturn> REQUESTED_ATTRIBUTES = new TreeSet<>();

    @BeforeClass
    public static void createRequestedAttributes() {
        REQUESTED_ATTRIBUTES.add(AttributeToReturn.SSN);
        REQUESTED_ATTRIBUTES.add(AttributeToReturn.BASIC_USER_INFO);
        REQUESTED_ATTRIBUTES.add(AttributeToReturn.CUSTOM_IDENTIFIER);
        REQUESTED_ATTRIBUTES.add(AttributeToReturn.DATE_OF_BIRTH);
        REQUESTED_ATTRIBUTES.add(AttributeToReturn.EMAIL_ADDRESS);
        REQUESTED_ATTRIBUTES.add(AttributeToReturn.INTEGRATOR_SPECIFIC_USER_ID);
        REQUESTED_ATTRIBUTES.add(AttributeToReturn.RELYING_PARTY_USER_ID);
        REQUESTED_ATTRIBUTES.add(AttributeToReturn.ORGANISATION_ID_IDENTIFIER);
        REQUESTED_ATTRIBUTES.add(AttributeToReturn.ADDRESSES);
        REQUESTED_ATTRIBUTES.add(AttributeToReturn.ALL_EMAIL_ADDRESSES);
        REQUESTED_ATTRIBUTES.add(AttributeToReturn.ALL_PHONE_NUMBERS);
        REQUESTED_ATTRIBUTES.add(AttributeToReturn.REGISTRATION_LEVEL);
        REQUESTED_ATTRIBUTES.add(AttributeToReturn.AGE);
        REQUESTED_ATTRIBUTES.add(AttributeToReturn.PHOTO);
        REQUESTED_ATTRIBUTES.add(AttributeToReturn.COVID_CERTIFICATES);
    }

    @Test
    public void createDefaultEmailRequest() {
        InitiateSignRequest expectedInitiateSignRequest =
                new InitiateSignRequest(UserInfoType.EMAIL, EMAIL, MinRegistrationLevel.PLUS, TITLE, null, null,
                                        DataToSignType.SIMPLE_UTF8_TEXT, DataToSign.create(TEXT),
                                        SignatureType.SIMPLE, null, null, null);
        InitiateSignRequest initiateSignRequest = InitiateSignRequest.createDefaultWithEmail(EMAIL, TITLE, TEXT);
        Assert.assertEquals(expectedInitiateSignRequest, initiateSignRequest);
    }

    @Test
    public void createDefaultSsnRequest() throws FrejaEidClientInternalException {
        InitiateSignRequest expectedInitiateSignRequest =
                new InitiateSignRequest(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(SSN_USER_INFO),
                                        MinRegistrationLevel.PLUS, TITLE, null, null, DataToSignType.SIMPLE_UTF8_TEXT,
                                        DataToSign.create(TEXT), SignatureType.SIMPLE, null, null, null);
        InitiateSignRequest initiateSignRequest = InitiateSignRequest.createDefaultWithSsn(SSN_USER_INFO, TITLE, TEXT);
        Assert.assertEquals(expectedInitiateSignRequest, initiateSignRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypeEmail() {
        InitiateSignRequest expectedInitiateSignRequest =
                new InitiateSignRequest(UserInfoType.EMAIL, EMAIL, REGISTRATION_STATE, TITLE, PUSH_NOTIFICATION,
                                        EXPIRY, DataToSignType.SIMPLE_UTF8_TEXT, DataToSign.create(TEXT),
                                        SignatureType.SIMPLE, REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, ORG_ID_ISSUER);
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
                .build();
        Assert.assertEquals(expectedInitiateSignRequest, initiateSignRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypeEmail_defaultRegistrationState() {
        InitiateSignRequest expectedInitiateSignRequest =
                new InitiateSignRequest(UserInfoType.EMAIL, EMAIL, MinRegistrationLevel.PLUS, TITLE, null, EXPIRY,
                                        DataToSignType.SIMPLE_UTF8_TEXT, DataToSign.create(TEXT),
                                        SignatureType.SIMPLE, REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, ORG_ID_ISSUER);
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
                new InitiateSignRequest(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(SSN_USER_INFO),
                                        REGISTRATION_STATE, TITLE, PUSH_NOTIFICATION, EXPIRY,
                                        DataToSignType.SIMPLE_UTF8_TEXT, DataToSign.create(TEXT),
                                        SignatureType.SIMPLE, REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, ORG_ID_ISSUER);
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
                                        SignatureType.SIMPLE, null, null, null);
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
                new InitiateSignRequest(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(SSN_USER_INFO),
                                        REGISTRATION_STATE, TITLE, PUSH_NOTIFICATION, EXPIRY,
                                        DataToSignType.EXTENDED_UTF8_TEXT, DataToSign.create(TEXT, TEXT.getBytes()),
                                        SignatureType.EXTENDED, REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, ORG_ID_ISSUER);
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
                new InitiateSignRequest(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(SSN_USER_INFO),
                                        REGISTRATION_STATE, TITLE, PUSH_NOTIFICATION, EXPIRY,
                                        DataToSignType.SIMPLE_UTF8_TEXT, DataToSign.create(TEXT),
                                        SignatureType.SIMPLE, REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, ORG_ID_ISSUER);
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
                new InitiateSignRequest(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(SSN_USER_INFO),
                                        REGISTRATION_STATE, TITLE, PUSH_NOTIFICATION, EXPIRY,
                                        DataToSignType.SIMPLE_UTF8_TEXT, DataToSign.create(TEXT, "".getBytes()),
                                        SignatureType.SIMPLE, REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, ORG_ID_ISSUER);
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
                new InitiateSignRequest(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(SSN_USER_INFO),
                                        MinRegistrationLevel.PLUS, TITLE, PUSH_NOTIFICATION, EXPIRY,
                                        DataToSignType.SIMPLE_UTF8_TEXT, DataToSign.create(TEXT),
                                        SignatureType.SIMPLE, REQUESTED_ATTRIBUTES, null, ORG_ID_ISSUER);
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
                new InitiateSignRequest(UserInfoType.ORG_ID, ORGANISATION_ID, REGISTRATION_STATE, TITLE,
                                        PUSH_NOTIFICATION, EXPIRY, DataToSignType.SIMPLE_UTF8_TEXT,
                                        DataToSign.create(TEXT), SignatureType.SIMPLE, REQUESTED_ATTRIBUTES,
                                        RELYING_PARTY_ID, ORG_ID_ISSUER);
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
                new InitiateSignRequest(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(SSN_USER_INFO),
                                        MinRegistrationLevel.PLUS, TITLE, PUSH_NOTIFICATION, EXPIRY,
                                        DataToSignType.SIMPLE_UTF8_TEXT, DataToSign.create(TEXT),
                                        SignatureType.SIMPLE, REQUESTED_ATTRIBUTES, RELYING_PARTY_ID, null);
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
}
