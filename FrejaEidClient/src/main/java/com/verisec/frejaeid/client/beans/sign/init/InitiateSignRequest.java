package com.verisec.frejaeid.client.beans.sign.init;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;
import com.verisec.frejaeid.client.beans.general.AttributeToReturnInfo;
import com.verisec.frejaeid.client.beans.general.OriginDeviceDetails;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.beans.sign.init.InitiateSignRequestBuilders.UserInfoBuilder;
import com.verisec.frejaeid.client.enums.*;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.util.UserInfoUtil;

import java.util.Objects;
import java.util.Set;

public class InitiateSignRequest implements RelyingPartyRequest {

    private final UserInfoType userInfoType;
    private final String userInfo;
    private final String title;
    private final MinRegistrationLevel minRegistrationLevel;
    private final PushNotification pushNotification;
    private final Long expiry;
    private final DataToSignType dataToSignType;
    private final DataToSign dataToSign;
    private final SignatureType signatureType;
    private final Set<AttributeToReturnInfo> attributesToReturn;
    private final String relyingPartyId;
    private final String orgIdIssuer;
    private final UserConfirmationMethod userConfirmationMethod;
    private final OriginDeviceDetails originDeviceDetails;
    private final boolean useDynamicQrCode;

    /**
     * Returns instance of {@linkplain InitiateSignRequest} with:
     * <br> {@linkplain UserInfoType} {@code EMAIL},
     * {@link MinRegistrationLevel} {@code PLUS}, default push notification,
     * default expiry time of two minutes and without binary data to sign and
     * {@link AttributeToReturnInfo}.
     *
     * @param email user's email for which transaction will be initiated. It
     *              cannot be {@code null} or empty.
     * @param title this is transaction title that will be shown to user through
     *              Freja eID mobile application.
     * @param text  data that user will sign by approving this transaction, also
     *              will be shown through Freja eID mobile application.
     * @return request
     */
    public static InitiateSignRequest createDefaultWithEmail(String email, String title, String text) {
        return new InitiateSignRequest(UserInfoType.EMAIL, email, MinRegistrationLevel.PLUS, title, null, null,
                                       DataToSignType.SIMPLE_UTF8_TEXT, DataToSign.create(text), SignatureType.SIMPLE,
                                       null, null, null, null, null, false);
    }

    /**
     * Returns instance of {@linkplain InitiateSignRequest} with:
     * <br> {@linkplain UserInfoType} {@code SSN},
     * {@link MinRegistrationLevel} {@code PLUS} , default push notification,
     * default expiry time of two minutes and without binary data to sign and
     * {@link AttributeToReturnInfo}.
     *
     * @param ssnUserInfo instance of {@linkplain SsnUserInfo} that contains
     *                    with personal number and country for which transaction will be initiated.
     *                    It cannot be {@code null}.
     * @param title       this is transaction title that will be shown to user through
     *                    Freja eID mobile application.
     * @param text        data that user will sign by approving this transaction, also
     *                    will be shown through Freja eID mobile application.
     * @return request
     * @throws FrejaEidClientInternalException if error occurs when generating
     *                                         JSON content from ssnUserInfo
     */
    public static InitiateSignRequest createDefaultWithSsn(SsnUserInfo ssnUserInfo, String title, String text)
            throws FrejaEidClientInternalException {
        return new InitiateSignRequest(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(ssnUserInfo),
                                       MinRegistrationLevel.PLUS, title, null, null, DataToSignType.SIMPLE_UTF8_TEXT,
                                       DataToSign.create(text), SignatureType.SIMPLE, null, null, null, null, null, false);
    }

    /**
     * Returns instance of {@linkplain InitiateSignRequest} with:
     * <br> {@linkplain UserInfoType} {@code UPI},
     * {@link MinRegistrationLevel} {@code PLUS}, default push notification,
     * default expiry time of two minutes and without binary data to sign and
     * {@link AttributeToReturnInfo}.
     *
     * @param upi user's unique personal identifier for which transaction will be initiated. It
     *              cannot be {@code null} or empty.
     * @param title this is transaction title that will be shown to user through
     *              Freja eID mobile application.
     * @param text  data that user will sign by approving this transaction, also
     *              will be shown through Freja eID mobile application.
     * @return request
     */
    public static InitiateSignRequest createDefaultWithUpi(String upi, String title, String text) {
        return new InitiateSignRequest(UserInfoType.UPI, upi, MinRegistrationLevel.PLUS, title, null, null,
                                       DataToSignType.SIMPLE_UTF8_TEXT, DataToSign.create(text), SignatureType.SIMPLE,
                                       null, null, null, null, null, false);
    }

    /**
     * Returns instance of builder that is used for creating
     * {@linkplain InitiateSignRequest} with custom request parameters.
     *
     * @return request builder
     */
    public static UserInfoBuilder createCustom() {
        return new UserInfoBuilder();
    }

    @JsonCreator
    InitiateSignRequest(
            @JsonProperty(value = "userInfoType") UserInfoType userInfoType,
            @JsonProperty(value = "userInfo") String userInfo,
            @JsonProperty(value = "minRegistrationLevel") MinRegistrationLevel minRegistrationLevel,
            @JsonProperty(value = "title") String title,
            @JsonProperty(value = "pushNotification") PushNotification pushNotification,
            @JsonProperty(value = "expiry") Long expiry,
            @JsonProperty(value = "dataToSignType") DataToSignType dataToSignType,
            @JsonProperty(value = "dataToSign") DataToSign dataToSign,
            @JsonProperty(value = "signatureType") SignatureType signatureType,
            @JsonProperty(value = "attributesToReturn") Set<AttributeToReturnInfo> attributesToReturn,
            @JsonProperty(value = "relyingPartyId") String relyingPartyId,
            @JsonProperty(value = "orgIdIssuer") String orgIdIssuer,
            @JsonProperty(value = "userConfirmationMethod") UserConfirmationMethod userConfirmationMethod,
            @JsonProperty(value = "originDeviceDetails") OriginDeviceDetails originDeviceDetails,
            @JsonProperty(value = "useDynamicQrCode") boolean useDynamicQrCode) {
        this.userInfoType = userInfoType;
        this.userInfo = userInfo;
        this.title = title;
        this.minRegistrationLevel = minRegistrationLevel;
        this.pushNotification = pushNotification;
        this.expiry = expiry;
        this.dataToSignType = dataToSignType;
        this.dataToSign = dataToSign;
        this.signatureType = signatureType;
        this.attributesToReturn = attributesToReturn;
        this.relyingPartyId = relyingPartyId;
        this.orgIdIssuer = orgIdIssuer;
        this.userConfirmationMethod = userConfirmationMethod;
        this.originDeviceDetails = originDeviceDetails;
        this.useDynamicQrCode = useDynamicQrCode;
    }

    public UserInfoType getUserInfoType() {
        return userInfoType;
    }

    public String getUserInfo() {
        return userInfo;
    }

    @JsonIgnore
    public String getRelyingPartyId() {
        return relyingPartyId;
    }

    public String getTitle() {
        return title;
    }

    public PushNotification getPushNotification() {
        return pushNotification;
    }

    public Long getExpiry() {
        return expiry;
    }

    public DataToSignType getDataToSignType() {
        return dataToSignType;
    }

    public DataToSign getDataToSign() {
        return dataToSign;
    }

    public SignatureType getSignatureType() {
        return signatureType;
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
        return Objects.hash(
                userInfoType, userInfo, title, minRegistrationLevel, pushNotification, expiry, dataToSignType,
                dataToSign, signatureType, relyingPartyId, orgIdIssuer, userConfirmationMethod, originDeviceDetails, useDynamicQrCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InitiateSignRequest)) return false;
        InitiateSignRequest that = (InitiateSignRequest) o;
        return useDynamicQrCode == that.useDynamicQrCode &&
                userInfoType == that.userInfoType &&
                Objects.equals(userInfo, that.userInfo) &&
                Objects.equals(title, that.title) &&
                minRegistrationLevel == that.minRegistrationLevel &&
                Objects.equals(pushNotification, that.pushNotification) &&
                Objects.equals(expiry, that.expiry) &&
                dataToSignType == that.dataToSignType &&
                Objects.equals(dataToSign, that.dataToSign) &&
                signatureType == that.signatureType &&
                Objects.equals(attributesToReturn, that.attributesToReturn) &&
                Objects.equals(relyingPartyId, that.relyingPartyId) &&
                Objects.equals(orgIdIssuer, that.orgIdIssuer) &&
                userConfirmationMethod == that.userConfirmationMethod &&
                Objects.equals(originDeviceDetails, that.originDeviceDetails);
    }

    @Override
    public String toString() {
        return "InitiateSignRequest{" +
                "userInfoType=" + userInfoType +
                ", userInfo='" + userInfo + '\'' +
                ", title='" + title + '\'' +
                ", minRegistrationLevel=" + minRegistrationLevel +
                ", pushNotification=" + pushNotification +
                ", expiry=" + expiry +
                ", dataToSignType=" + dataToSignType +
                ", dataToSign=" + dataToSign +
                ", signatureType=" + signatureType +
                ", attributesToReturn=" + attributesToReturn +
                ", relyingPartyId='" + relyingPartyId + '\'' +
                ", orgIdIssuer='" + orgIdIssuer + '\'' +
                ", userConfirmationMethod=" + userConfirmationMethod +
                ", originDeviceDetails=" + originDeviceDetails +
                ", useDynamicQrCode=" + useDynamicQrCode +
                '}';
    }
}
