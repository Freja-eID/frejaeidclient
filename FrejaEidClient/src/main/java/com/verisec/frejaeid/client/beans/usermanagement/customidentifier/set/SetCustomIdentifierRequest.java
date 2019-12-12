package com.verisec.frejaeid.client.beans.usermanagement.customidentifier.set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.beans.usermanagement.customidentifier.set.SetCustomIdentifierRequestBuilder.SetParamsBuilder;
import com.verisec.frejaeid.client.enums.UserInfoType;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.util.UserInfoUtil;
import java.util.Objects;

public class SetCustomIdentifierRequest implements RelyingPartyRequest {

    private final UserInfoType userInfoType;
    private final String userInfo;
    private final String customIdentifier;
    private final String relyingPartyId;

    /**
     * Returns instance of {@linkplain SetCustomIdentifierRequest} with
     * {@linkplain UserInfoType} {@code EMAIL}.
     *
     * @param email will be used to identify the end user. It cannot be
     * {@code null} or empty. Maximum length is 256 characters.
     * @param customIdentifier will be set for user. Maximum length is 128
     * characters. It cannot be {@code null} or empty.
     *
     * @return request
     */
    public static SetCustomIdentifierRequest createDefaultWithEmail(String email, String customIdentifier) {
        return new SetCustomIdentifierRequest(UserInfoType.EMAIL, email, customIdentifier, null);
    }

    /**
     * Returns instance of {@linkplain SetCustomIdentifierRequest} with
     * {@linkplain UserInfoType} {@code SSN}.
     *
     * @param ssnUserInfo instance of {@linkplain SsnUserInfo} that contains
     * personal number and country that will be used to identify the end user.
     * It cannot be {@code null}.
     * @param customIdentifier will be set for user. Maximum length is 128
     * characters. It cannot be {@code null} or empty.
     *
     * @return request
     * @throws FrejaEidClientInternalException if error occurs when generating
     * JSON content from ssnUserInfo
     *
     */
    public static SetCustomIdentifierRequest createDefaultWithSsn(SsnUserInfo ssnUserInfo, String customIdentifier) throws FrejaEidClientInternalException {
        return new SetCustomIdentifierRequest(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(ssnUserInfo), customIdentifier, null);
    }

    /**
     * Returns instance of builder that is used for creating
     * {@linkplain SetCustomIdentifierRequest} with custom request parameters.
     *
     * @return request builder
     */
    public static SetParamsBuilder createCustom() {
        return new SetParamsBuilder();
    }

    @JsonCreator
    private SetCustomIdentifierRequest(@JsonProperty(value = "userInfoType") UserInfoType userInfoType, @JsonProperty(value = "userInfo") String userInfo, @JsonProperty(value = "customIdentifier") String customIdentifier) {
        this(userInfoType, userInfo, customIdentifier, null);
    }

    SetCustomIdentifierRequest(UserInfoType userInfoType, String userInfo, String customIdentifier, String relyingPartyId) {
        this.userInfo = userInfo;
        this.userInfoType = userInfoType;
        this.customIdentifier = customIdentifier;
        this.relyingPartyId = relyingPartyId;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public UserInfoType getUserInfoType() {
        return userInfoType;
    }

    public String getCustomIdentifier() {
        return customIdentifier;
    }

    @JsonIgnore
    public String getRelyingPartyId() {
        return relyingPartyId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userInfoType, userInfo, customIdentifier, relyingPartyId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SetCustomIdentifierRequest other = (SetCustomIdentifierRequest) obj;
        if (!Objects.equals(this.userInfo, other.userInfo)) {
            return false;
        }
        if (!Objects.equals(this.customIdentifier, other.customIdentifier)) {
            return false;
        }
        if (!Objects.equals(this.relyingPartyId, other.relyingPartyId)) {
            return false;
        }
        if (this.userInfoType != other.userInfoType) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SetCustomIdentifierRequest{" + "userInfoType=" + userInfoType + ", userInfo=" + userInfo + ", customIdentifier=" + customIdentifier + '}';
    }

}
