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
    private final boolean useDynamicQrCode;

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
                UserInfoType.EMAIL, email, MinRegistrationLevel.BASIC, null, null, null, null, false);
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
                                                 MinRegistrationLevel.BASIC, null, null, null, null, false);
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
            @JsonProperty(value = "originDeviceDetails") OriginDeviceDetails originDeviceDetails,
            @JsonProperty(value = "useDynamicQrCode") boolean useDynamicQrCode) {
        this(userInfoType, userInfo, minRegistrationLevel, attributesToReturn, null, orgIdIssuer,
             userConfirmationMethod, originDeviceDetails, useDynamicQrCode);
    }

    InitiateAuthenticationRequest(
            UserInfoType userInfoType, String userInfo, MinRegistrationLevel minRegistrationLevel,
            Set<AttributeToReturnInfo> attributesToReturn, String relyingPartyId,
            String orgIdIssuer, UserConfirmationMethod userConfirmationMethod,
            OriginDeviceDetails originDeviceDetails, boolean useDynamicQrCode) {
        this.userInfoType = userInfoType;
        this.userInfo = userInfo;
        this.minRegistrationLevel = minRegistrationLevel;
        this.attributesToReturn = attributesToReturn;
        this.relyingPartyId = relyingPartyId;
        this.orgIdIssuer = orgIdIssuer;
        this.userConfirmationMethod = userConfirmationMethod;
        this.originDeviceDetails = originDeviceDetails;
        this.useDynamicQrCode = useDynamicQrCode;
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

    public boolean isUseDynamicQrCode() {
        return useDynamicQrCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userInfoType, userInfo, minRegistrationLevel, attributesToReturn, relyingPartyId,
                            orgIdIssuer, userConfirmationMethod, originDeviceDetails, useDynamicQrCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InitiateAuthenticationRequest)) return false;
        InitiateAuthenticationRequest that = (InitiateAuthenticationRequest) o;
        return useDynamicQrCode == that.useDynamicQrCode &&
                userInfoType == that.userInfoType &&
                Objects.equals(userInfo, that.userInfo) &&
                minRegistrationLevel == that.minRegistrationLevel &&
                Objects.equals(attributesToReturn, that.attributesToReturn) &&
                Objects.equals(relyingPartyId, that.relyingPartyId) &&
                Objects.equals(orgIdIssuer, that.orgIdIssuer) &&
                userConfirmationMethod == that.userConfirmationMethod &&
                Objects.equals(originDeviceDetails, that.originDeviceDetails);
    }

    @Override
    public String toString() {
        return "InitiateAuthenticationRequest{" +
                "userInfoType=" + userInfoType +
                ", userInfo='" + userInfo + '\'' +
                ", minRegistrationLevel=" + minRegistrationLevel +
                ", attributesToReturn=" + attributesToReturn +
                ", relyingPartyId='" + relyingPartyId + '\'' +
                ", orgIdIssuer='" + orgIdIssuer + '\'' +
                ", userConfirmationMethod=" + userConfirmationMethod +
                ", originDeviceDetails=" + originDeviceDetails +
                ", useDynamicQrCode=" + useDynamicQrCode +
                '}';
    }
}
