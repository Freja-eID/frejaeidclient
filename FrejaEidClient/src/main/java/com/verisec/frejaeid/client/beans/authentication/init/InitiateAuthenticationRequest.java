package com.verisec.frejaeid.client.beans.authentication.init;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.authentication.init.InitiateAuthenticationRequestBuilders.UserInfoBuilder;
import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.beans.general.AttributeToReturnInfo;
import com.verisec.frejaeid.client.beans.general.OriginDeviceDetails;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.enums.MinRegistrationLevel;
import com.verisec.frejaeid.client.enums.UserConfirmationMethod;
import com.verisec.frejaeid.client.enums.UserInfoType;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.util.UserInfoUtil;

import java.util.Objects;
import java.util.Set;

public class InitiateAuthenticationRequest implements RelyingPartyRequest {

    private final UserInfoType userInfoType;
    private final String userInfo;
    private final MinRegistrationLevel minRegistrationLevel;
    private final Set<AttributeToReturnInfo> attributesToReturn;
    private final String relyingPartyId;
    private final String orgIdIssuer;
    private final UserConfirmationMethod userConfirmationMethod;
    private final OriginDeviceDetails originDeviceDetails;

    /**
     * Returns instance of {@linkplain InitiateAuthenticationRequest} with:
     * <br> {@linkplain UserInfoType} {@code EMAIL}, {@link MinRegistrationLevel} {@code BASIC} and without
     * {@link AttributeToReturnInfo}.
     *
     * @param email user's email for which transaction will be initiated. It cannot be {@code null} or empty. Maximum
     *              length is 256 characters.
     * @return request
     */
    public static InitiateAuthenticationRequest createDefaultWithEmail(String email) {
        return new InitiateAuthenticationRequest(
                UserInfoType.EMAIL, email, MinRegistrationLevel.BASIC, null, null, null, null);
    }

    /**
     * Returns instance of {@linkplain InitiateAuthenticationRequest} with:
     * <br> {@linkplain UserInfoType} {@code SSN}, {@link MinRegistrationLevel} {@code BASIC} and without
     * {@link AttributeToReturnInfo}.
     *
     * @param ssnUserInfo instance of {@linkplain SsnUserInfo} that contains personal number and country for which
     *                    transaction will be initiated. It cannot be {@code null}.
     * @return request
     * @throws FrejaEidClientInternalException if error occurs when generating JSON content from ssnUserInfo
     */
    public static InitiateAuthenticationRequest createDefaultWithSsn(SsnUserInfo ssnUserInfo)
            throws FrejaEidClientInternalException {
        return new InitiateAuthenticationRequest(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(ssnUserInfo),
                                                 MinRegistrationLevel.BASIC, null, null, null, null);
    }

    /**
     * Returns instance of builder that is used for creating {@linkplain InitiateAuthenticationRequest} with custom
     * request parameters.
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
            @JsonProperty(value = "attributesToReturn") Set<AttributeToReturnInfo> attributesToReturn,
            @JsonProperty(value = "orgIdIssuer") String orgIdIssuer,
            @JsonProperty(value = "userConfirmationMethod") UserConfirmationMethod userConfirmationMethod,
            @JsonProperty(value = "originDeviceDetails") OriginDeviceDetails originDeviceDetails) {
        this(userInfoType, userInfo, minRegistrationLevel, attributesToReturn, null, orgIdIssuer,
             userConfirmationMethod, originDeviceDetails);
    }

    InitiateAuthenticationRequest(
            UserInfoType userInfoType, String userInfo, MinRegistrationLevel minRegistrationLevel,
            Set<AttributeToReturnInfo> attributesToReturn, String relyingPartyId,
            String orgIdIssuer, UserConfirmationMethod userConfirmationMethod,
            OriginDeviceDetails originDeviceDetails) {
        this.userInfoType = userInfoType;
        this.userInfo = userInfo;
        this.minRegistrationLevel = minRegistrationLevel;
        this.attributesToReturn = attributesToReturn;
        this.relyingPartyId = relyingPartyId;
        this.orgIdIssuer = orgIdIssuer;
        this.userConfirmationMethod = userConfirmationMethod;
        this.originDeviceDetails = originDeviceDetails;

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

    public Set<AttributeToReturnInfo> getAttributesToReturn() {
        return attributesToReturn;
    }

    public String getOrgIdIssuer() {
        return orgIdIssuer;
    }

    public UserConfirmationMethod getUserConfirmationMethod() {
        return userConfirmationMethod;
    }

    public OriginDeviceDetails getOriginDeviceDetails() {
        return originDeviceDetails;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userInfoType, userInfo, minRegistrationLevel, attributesToReturn, relyingPartyId,
                            orgIdIssuer, userConfirmationMethod, originDeviceDetails);
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
        if (!Objects.equals(this.orgIdIssuer, other.orgIdIssuer)) {
            return false;
        }
        if (this.userInfoType != other.userInfoType) {
            return false;
        }
        if (this.minRegistrationLevel != other.minRegistrationLevel) {
            return false;
        }
        if (this.attributesToReturn != null && other.attributesToReturn != null) {
            if (this.attributesToReturn.size() != other.attributesToReturn.size()) {
                return false;
            }
            if (!this.attributesToReturn.containsAll(other.attributesToReturn)) {
                return false;
            }
        } else if (this.attributesToReturn != null || other.attributesToReturn != null) {
            return false;
        }
        if (this.userConfirmationMethod != other.userConfirmationMethod) {
            return false;
        }
        if (!Objects.equals(this.originDeviceDetails, other.originDeviceDetails)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InitiateAuthenticationRequest{" + "userInfoType=" + userInfoType + ", userInfo='" + userInfo + '\''
                + ", minRegistrationLevel=" + minRegistrationLevel + ", attributesToReturn=" + attributesToReturn
                + ", relyingPartyId='" + relyingPartyId + '\'' + ", orgIdIssuer='" + orgIdIssuer + '\''
                + ", userConfirmationMethod=" + userConfirmationMethod
                + ", originDeviceDetails=" + originDeviceDetails
                + '}';
    }
}
