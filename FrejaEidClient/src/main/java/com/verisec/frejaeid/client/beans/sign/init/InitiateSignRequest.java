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
                                       null, null, null, null, null);
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
                                       DataToSign.create(text), SignatureType.SIMPLE, null, null, null, null, null);
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
            @JsonProperty(value = "originDeviceDetails") OriginDeviceDetails originDeviceDetails) {
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

    @Override
    public int hashCode() {
        return Objects.hash(
                userInfoType, userInfo, title, minRegistrationLevel, pushNotification, expiry, dataToSignType,
                dataToSign, signatureType, relyingPartyId, orgIdIssuer, userConfirmationMethod, originDeviceDetails);
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
        final InitiateSignRequest other = (InitiateSignRequest) obj;
        if (!Objects.equals(this.userInfo, other.userInfo)) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
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
        if (!Objects.equals(this.pushNotification, other.pushNotification)) {
            return false;
        }
        if (!Objects.equals(this.expiry, other.expiry)) {
            return false;
        }
        if (this.dataToSignType != other.dataToSignType) {
            return false;
        }
        if (!Objects.equals(this.dataToSign, other.dataToSign)) {
            return false;
        }
        if (this.signatureType != other.signatureType) {
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
                '}';
    }
}
