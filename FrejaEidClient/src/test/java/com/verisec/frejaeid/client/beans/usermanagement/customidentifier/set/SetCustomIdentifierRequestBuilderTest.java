package com.verisec.frejaeid.client.beans.usermanagement.customidentifier.set;

import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.enums.Country;
import com.verisec.frejaeid.client.enums.UserInfoType;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.util.UserInfoUtil;
import org.junit.Assert;
import org.junit.Test;

public class SetCustomIdentifierRequestBuilderTest {

    private static final String EMAIL = "email";
    private static final SsnUserInfo SSN_USER_INFO = SsnUserInfo.create(Country.SWEDEN, "123123123");
    private static final String PHONE_NUMBER = "123123123";
    private static final String IDENTIFIER = "identifier";
    private static final String RELYING_PARTY_ID = "relyingPartyId";

    @Test
    public void createDefaultEmailRequest() {
        SetCustomIdentifierRequest expectedSetCustomIdentifierRequest =
                new SetCustomIdentifierRequest(UserInfoType.EMAIL, EMAIL, IDENTIFIER, null);
        SetCustomIdentifierRequest setCustomIdentifierRequest =
                SetCustomIdentifierRequest.createDefaultWithEmail(EMAIL, IDENTIFIER);
        Assert.assertEquals(expectedSetCustomIdentifierRequest, setCustomIdentifierRequest);
    }

    @Test
    public void createDefaultSsnRequest() throws FrejaEidClientInternalException {
        SetCustomIdentifierRequest expectedSetCustomIdentifierRequest =
                new SetCustomIdentifierRequest(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(SSN_USER_INFO),
                                               IDENTIFIER, null);
        SetCustomIdentifierRequest setCustomIdentifierRequest =
                SetCustomIdentifierRequest.createDefaultWithSsn(SSN_USER_INFO, IDENTIFIER);
        Assert.assertEquals(expectedSetCustomIdentifierRequest, setCustomIdentifierRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypeEmail() {
        SetCustomIdentifierRequest expectedSetCustomIdentifierRequest =
                new SetCustomIdentifierRequest(UserInfoType.EMAIL, EMAIL, IDENTIFIER, RELYING_PARTY_ID);
        SetCustomIdentifierRequest setCustomIdentifierRequest = SetCustomIdentifierRequest.createCustom()
                .setEmailAndCustomIdentifier(EMAIL, IDENTIFIER)
                .setRelyingPartyId(RELYING_PARTY_ID)
                .build();
        Assert.assertEquals(expectedSetCustomIdentifierRequest, setCustomIdentifierRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypeSsn() throws FrejaEidClientInternalException {
        SetCustomIdentifierRequest expectedSetCustomIdentifierRequest =
                new SetCustomIdentifierRequest(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(SSN_USER_INFO),
                                               IDENTIFIER, RELYING_PARTY_ID);
        SetCustomIdentifierRequest setCustomIdentifierRequest = SetCustomIdentifierRequest.createCustom()
                .setSsnAndCustomIdentifier(SSN_USER_INFO, IDENTIFIER)
                .setRelyingPartyId(RELYING_PARTY_ID)
                .build();
        Assert.assertEquals(expectedSetCustomIdentifierRequest, setCustomIdentifierRequest);
    }

    @Test
    public void createCustomRequest_userInfoTypePhoneNumber() {
        SetCustomIdentifierRequest expectedSetCustomIdentifierRequest =
                new SetCustomIdentifierRequest(UserInfoType.PHONE, PHONE_NUMBER, IDENTIFIER, RELYING_PARTY_ID);
        SetCustomIdentifierRequest setCustomIdentifierRequest = SetCustomIdentifierRequest.createCustom()
                .setPhoneNumberAndCustomIdentifier(PHONE_NUMBER, IDENTIFIER)
                .setRelyingPartyId(RELYING_PARTY_ID)
                .build();
        Assert.assertEquals(expectedSetCustomIdentifierRequest, setCustomIdentifierRequest);
    }

    @Test
    public void createCustomRequest_relyingPartyIdNull() {
        SetCustomIdentifierRequest expectedSetCustomIdentifierRequest =
                new SetCustomIdentifierRequest(UserInfoType.PHONE, PHONE_NUMBER, IDENTIFIER, null);
        SetCustomIdentifierRequest setCustomIdentifierRequest = SetCustomIdentifierRequest.createCustom()
                .setPhoneNumberAndCustomIdentifier(PHONE_NUMBER, IDENTIFIER)
                .setRelyingPartyId(null)
                .build();
        Assert.assertEquals(expectedSetCustomIdentifierRequest, setCustomIdentifierRequest);
    }
}
