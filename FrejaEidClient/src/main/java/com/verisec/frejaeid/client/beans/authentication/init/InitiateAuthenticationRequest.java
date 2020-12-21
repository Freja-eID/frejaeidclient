package com.verisec.frejaeid.client.beans.authentication.init;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.beans.authentication.init.InitiateAuthenticationRequestBuilders.UserInfoBuilder;
import com.verisec.frejaeid.client.enums.AttributeToReturn;
import com.verisec.frejaeid.client.enums.MinRegistrationLevel;
import com.verisec.frejaeid.client.enums.UserInfoType;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.util.UserInfoUtil;

import java.util.Objects;
import java.util.Set;

public class InitiateAuthenticationRequest implements RelyingPartyRequest {

    private final UserInfoType userInfoType;
    private final String userInfo;
    private final MinRegistrationLevel minRegistrationLevel;
    private final Set<AttributeToReturn> attributesToReturn;
    private final String relyingPartyId;

    /**
     * Returns instance of {@linkplain InitiateAuthenticationRequest} with:
     * <br> {@linkplain UserInfoType} {@code EMAIL},
     * {@link MinRegistrationLevel} {@code BASIC} and without
     * {@linkplain AttributeToReturn}.
     *
     * @param email user's email for which transaction will be initiated. It
     *              cannot be {@code null} or empty. Maximum length is 256 characters.
     * @return request
     */
    public static InitiateAuthenticationRequest createDefaultWithEmail(String email) {
        return new InitiateAuthenticationRequest(UserInfoType.EMAIL, email, MinRegistrationLevel.BASIC, null, null);
    }

    /**
     * Returns instance of {@linkplain InitiateAuthenticationRequest} with:
     * <br> {@linkplain UserInfoType} {@code SSN},
     * {@link MinRegistrationLevel} {@code BASIC} and without
     * {@linkplain AttributeToReturn}.
     *
     * @param ssnUserInfo instance of {@linkplain SsnUserInfo} that contains
     *                    personal number and country for which transaction will be initiated. It
     *                    cannot be {@code null}.
     * @return request
     * @throws FrejaEidClientInternalException if error occurs when generating
     *                                         JSON content from ssnUserInfo
     */
    public static InitiateAuthenticationRequest createDefaultWithSsn(SsnUserInfo ssnUserInfo)
            throws FrejaEidClientInternalException {
        return new InitiateAuthenticationRequest(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(ssnUserInfo),
                                                 MinRegistrationLevel.BASIC, null, null);
    }

    /**
     * Returns instance of builder that is used for creating
     * {@linkplain InitiateAuthenticationRequest} with custom request
     * parameters.
     *
     * @return request builder
     */
    public static UserInfoBuilder createCustom() {
        return new UserInfoBuilder();
    }

    @JsonCreator
    private InitiateAuthenticationRequest(
            @JsonProperty(value = "userInfoType") UserInfoType userInfoType,
            @JsonProperty(value = "userInfo") String userInfo,
            @JsonProperty(value = "minRegistrationLevel") MinRegistrationLevel minRegistrationLevel,
            @JsonProperty(value = "attributesToReturn") Set<AttributeToReturn> attributesToReturn) {
        this(userInfoType, userInfo, minRegistrationLevel, attributesToReturn, null);
    }

    InitiateAuthenticationRequest(UserInfoType userInfoType, String userInfo,
                                  MinRegistrationLevel minRegistrationLevel,
                                  Set<AttributeToReturn> attributesToReturn, String relyingPartyId) {
        this.userInfoType = userInfoType;
        this.userInfo = userInfo;
        this.minRegistrationLevel = minRegistrationLevel;
        this.attributesToReturn = attributesToReturn;
        this.relyingPartyId = relyingPartyId;
    }

    @JsonIgnore
    public String getRelyingPartyId() {
        return relyingPartyId;
    }

    public UserInfoType getUserInfoType() {
        return userInfoType;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public MinRegistrationLevel getMinRegistrationLevel() {
        return minRegistrationLevel;
    }

    public Set<AttributeToReturn> getAttributesToReturn() {
        return attributesToReturn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userInfoType, userInfo, minRegistrationLevel, attributesToReturn, relyingPartyId);
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
        final InitiateAuthenticationRequest other = (InitiateAuthenticationRequest) obj;
        if (!Objects.equals(this.userInfo, other.userInfo)) {
            return false;
        }
        if (!Objects.equals(this.relyingPartyId, other.relyingPartyId)) {
            return false;
        }
        if (this.userInfoType != other.userInfoType) {
            return false;
        }
        if (this.minRegistrationLevel != other.minRegistrationLevel) {
            return false;
        }
        if (!Objects.equals(this.attributesToReturn, other.attributesToReturn)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InitiateAuthenticationRequest{" + "userInfoType=" + userInfoType + ", userInfo=" + userInfo + ", " +
                "minRegistrationLevel=" + minRegistrationLevel + ", attributesToReturn=" + attributesToReturn + '}';
    }

}
