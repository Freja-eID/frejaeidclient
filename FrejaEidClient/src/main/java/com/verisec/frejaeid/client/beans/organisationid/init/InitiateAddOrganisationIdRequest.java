package com.verisec.frejaeid.client.beans.organisationid.init;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.beans.general.OrganisationId;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.beans.organisationid.init.InitiateAddOrganisationIdRequestBuilders.SetParamsBuilder;
import com.verisec.frejaeid.client.enums.MinRegistrationLevel;
import com.verisec.frejaeid.client.enums.UserInfoType;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.util.UserInfoUtil;

import java.util.Objects;

public class InitiateAddOrganisationIdRequest implements RelyingPartyRequest {

    private final UserInfoType userInfoType;
    private final String userInfo;
    private final OrganisationId organisationId;
    private final MinRegistrationLevel minRegistrationLevel;
    private final Long expiry;
    private final String text;
    private final String relyingPartyId;

    /**
     * Returns instance of {@linkplain InitiateAddOrganisationIdRequest} with
     * {@linkplain UserInfoType} {@code EMAIL}.
     *
     * @param email          user's email for which transaction will be initiated. It
     *                       cannot be {@code null} or empty. Maximum length is 256 characters.
     * @param organisationId instance of {@linkplain OrganisationId} that
     *                       contains information regarding organisation id.
     * @return request
     */
    public static InitiateAddOrganisationIdRequest createDefaultWithEmail(String email, OrganisationId organisationId) {
        return new InitiateAddOrganisationIdRequest(UserInfoType.EMAIL, email, organisationId,
                MinRegistrationLevel.EXTENDED, null, null);
    }

    /**
     * Returns instance of {@linkplain InitiateAddOrganisationIdRequest} with
     * {@linkplain UserInfoType} {@code SSN}.
     *
     * @param ssnUserInfo    instance of {@linkplain SsnUserInfo} that contains
     *                       personal number and country for which transaction will be initiated. It
     *                       cannot be {@code null}.
     * @param organisationId instance of {@linkplain OrganisationId} that
     *                       contains information regarding organisation id.
     * @return request
     * @throws FrejaEidClientInternalException if error occurs when generating
     *                                         JSON content from ssnUserInfo
     */
    public static InitiateAddOrganisationIdRequest createDefaultWithSsn(SsnUserInfo ssnUserInfo,
                                                                        OrganisationId organisationId)
            throws FrejaEidClientInternalException {
        return new InitiateAddOrganisationIdRequest(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(ssnUserInfo),
                organisationId, MinRegistrationLevel.EXTENDED, null, null);
    }

    /**
     * Returns instance of {@linkplain InitiateAddOrganisationIdRequest} with
     * {@linkplain UserInfoType} {@code UPI}.
     *
     * @param upi          user's unique personal identifier for which transaction will be initiated. It
     *                       cannot be {@code null} or empty. Maximum length is 256 characters.
     * @param organisationId instance of {@linkplain OrganisationId} that
     *                       contains information regarding organisation id.
     * @return request
     */
    public static InitiateAddOrganisationIdRequest createDefaultWithUpi(String upi, OrganisationId organisationId) {
        return new InitiateAddOrganisationIdRequest(UserInfoType.UPI, upi, organisationId,
                                                    MinRegistrationLevel.EXTENDED, null, null);
    }

    /**
     * Returns instance of builder that is used for creating
     * {@linkplain InitiateAddOrganisationIdRequest} with custom request
     * parameters.
     *
     * @return request builder
     */
    public static SetParamsBuilder createCustom() {
        return new SetParamsBuilder();
    }

    @JsonCreator
    private InitiateAddOrganisationIdRequest(
            @JsonProperty(value = "userInfoType") UserInfoType userInfoType,
            @JsonProperty(value = "userInfo") String userInfo,
            @JsonProperty(value = "organisationId") OrganisationId organisationId,
            @JsonProperty(value = "minRegistrationLevel") MinRegistrationLevel minRegistrationLevel,
            @JsonProperty(value = "expiry") Long expiry,
            @JsonProperty(value = "text") String text) {
        this(userInfoType, userInfo, organisationId, minRegistrationLevel, expiry, text, null);
    }

    InitiateAddOrganisationIdRequest(UserInfoType userInfoType, String userInfo,
                                     OrganisationId organisationId, MinRegistrationLevel minRegistrationLevel,
                                     Long expiry, String text, String relyingPartyId) {
        this.userInfoType = userInfoType;
        this.userInfo = userInfo;
        this.organisationId = organisationId;
        this.minRegistrationLevel = minRegistrationLevel;
        this.expiry = expiry;
        this.text = text;
        this.relyingPartyId = relyingPartyId;
    }

    public UserInfoType getUserInfoType() {
        return userInfoType;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public OrganisationId getOrganisationId() {
        return organisationId;
    }

    public MinRegistrationLevel getMinRegistrationLevel() {
        return minRegistrationLevel;
    }

    public Long getExpiry() {
        return expiry;
    }

    public String getText() {
        return text;
    }

    @JsonIgnore
    public String getRelyingPartyId() {
        return relyingPartyId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userInfoType, userInfo, organisationId, minRegistrationLevel,
                expiry, text, relyingPartyId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InitiateAddOrganisationIdRequest)) return false;
        InitiateAddOrganisationIdRequest that = (InitiateAddOrganisationIdRequest) o;
        return userInfoType == that.userInfoType &&
                Objects.equals(userInfo, that.userInfo) &&
                Objects.equals(organisationId, that.organisationId) &&
                minRegistrationLevel == that.minRegistrationLevel &&
                Objects.equals(expiry, that.expiry) &&
                Objects.equals(text, that.text) &&
                Objects.equals(relyingPartyId, that.relyingPartyId);
    }

    @Override
    public String toString() {
        return "InitiateAddOrganisationIdRequest{" +
                "userInfoType=" + userInfoType +
                ", userInfo='" + userInfo + '\'' +
                ", organisationId=" + organisationId +
                ", minRegistrationLevel=" + minRegistrationLevel +
                ", expiry=" + expiry +
                ", text=" + text +
                ", relyingPartyId='" + relyingPartyId + '\'' +
                '}';
    }
}
