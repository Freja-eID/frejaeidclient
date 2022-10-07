package com.verisec.frejaeid.client.beans.sign.init;

import com.verisec.frejaeid.client.beans.general.AttributeToReturnInfo;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.enums.AttributeToReturn;
import com.verisec.frejaeid.client.enums.DataToSignType;
import com.verisec.frejaeid.client.enums.MinRegistrationLevel;
import com.verisec.frejaeid.client.enums.SignatureType;
import com.verisec.frejaeid.client.enums.TransactionContext;
import com.verisec.frejaeid.client.enums.UserInfoType;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.util.UserInfoUtil;

import java.util.HashSet;
import java.util.Set;

public class InitiateSignRequestBuilders {

    public static class UserInfoBuilder {

        /**
         * Sets email as userInfo and {@linkplain UserInfoType#EMAIL} as
         * {@linkplain UserInfoType} for initiating transaction.
         *
         * @param email user's email that will be used to identify the end user.
         *              Maximum length is 256 characters.
         * @return request builder
         */
        public SetOptionalParamsBuilder setEmail(String email) {
            return new SetOptionalParamsBuilder(UserInfoType.EMAIL, email);
        }

        /**
         * Sets ssnUserInfo as userInfo and {@linkplain UserInfoType#SSN} as
         * {@linkplain UserInfoType} for initiating transaction.
         *
         * @param ssnUserInfo instance of {@linkplain SsnUserInfo} contains
         *                    personal number and country of user that will be used to identify the
         *                    end user.
         * @return request builder
         * @throws FrejaEidClientInternalException if error occurs when
         *                                         generating JSON content from ssnUserInfo
         */
        public SetOptionalParamsBuilder setSsn(SsnUserInfo ssnUserInfo) throws FrejaEidClientInternalException {
            return new SetOptionalParamsBuilder(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(ssnUserInfo));
        }

        /**
         * Sets phoneNumber as userInfo and {@link UserInfoType#PHONE} as
         * {@linkplain UserInfoType} for initiating transaction.
         *
         * @param phoneNumber user's phone number that will be used to identify
         *                    the end user in format +467123456789.
         * @return request builder
         */
        public SetOptionalParamsBuilder setPhoneNumber(String phoneNumber) {
            return new SetOptionalParamsBuilder(UserInfoType.PHONE, phoneNumber);
        }
        
        /**
         * Sets {@linkplain UserInfoType#INFERRED} as {@linkplain UserInfoType}
         * for initiating transaction.
         *
         * @return request builder
         */
        public SetOptionalParamsBuilder setInferred() {
            return new SetOptionalParamsBuilder(UserInfoType.INFERRED, "N/A");
        }

        /**
         * Sets organisation id as userInfo and {@link UserInfoType#ORG_ID} as
         * {@linkplain UserInfoType} for initiating transaction.
         *
         * @param identifier user's identifier that was previously set by
         *                   organisation
         * @return request builder
         */
        public SetOptionalParamsBuilder setOrganisationId(String identifier) {
            return new SetOptionalParamsBuilder(UserInfoType.ORG_ID, identifier);
        }

    }

    public static class SetOptionalParamsBuilder {

        private final UserInfoType userInfoType;
        private final String userInfo;
        private MinRegistrationLevel minRegistrationLevel = MinRegistrationLevel.PLUS;
        private String title = null;
        private PushNotification pushNotification = null;
        private Long expiry = null;
        private DataToSignType dataToSignType = DataToSignType.SIMPLE_UTF8_TEXT;
        private DataToSign dataToSign;
        private SignatureType signatureType = SignatureType.SIMPLE;
        private Set<AttributeToReturnInfo> attributesToReturn = null;
        private String relyingPartyId = null;
        private String orgIdIssuer = null;

        private SetOptionalParamsBuilder(UserInfoType userInfoType, String userInfo) {
            this.userInfoType = userInfoType;
            this.userInfo = userInfo;
        }

        /**
         * Minimum registration level is only used with
         * {@link TransactionContext#PERSONAL}. It is optional, default value is
         * {@link MinRegistrationLevel#PLUS}.
         *
         * @param minRegistrationLevel will be set as minimum required
         *                             registration level for user in order to approve/decline signing.
         * @return request builder
         */
        public SetOptionalParamsBuilder setMinRegistrationLevel(MinRegistrationLevel minRegistrationLevel) {
            if (minRegistrationLevel == null) {
                minRegistrationLevel = MinRegistrationLevel.PLUS;
            }
            this.minRegistrationLevel = minRegistrationLevel;
            return this;
        }

        /**
         * Attributes to return are attributes that are requested from user to
         * share.
         *
         * @param attributesToReturn can be any value from
         *                           {@linkplain AttributeToReturn}
         * @return request builder
         */
        public SetOptionalParamsBuilder setAttributesToReturn(AttributeToReturn... attributesToReturn) {
            this.attributesToReturn = new HashSet();
            for (AttributeToReturn attributeToReturn : attributesToReturn) {
                this.attributesToReturn.add(
                        new AttributeToReturnInfo(attributeToReturn.getName()));
            }
            return this;
        }

        /**
         * Transaction title will be shown to user through Freja eID mobile
         * application.
         *
         * @param title optional parameter, but if set it cannot be empty.
         *              Maximum length is 128 characters.
         * @return request builder
         */
        public SetOptionalParamsBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Data to sign can be only text (SIMPLE signature type) or text and
         * binary data (EXTENDED signature type).
         *
         * @param dataToSign mandatory parameter. Text cannot be {@code null} or
         *                   empty.
         * @return request builder
         */
        public SetOptionalParamsBuilder setDataToSign(DataToSign dataToSign) {

            if (dataToSign == null) {
                return this;
            }
            if (dataToSign.getBinaryData() == null || dataToSign.getBinaryData().isEmpty()) {
                this.dataToSignType = DataToSignType.SIMPLE_UTF8_TEXT;
                this.signatureType = SignatureType.SIMPLE;
            } else {
                this.dataToSignType = DataToSignType.EXTENDED_UTF8_TEXT;
                this.signatureType = SignatureType.EXTENDED;
            }
            this.dataToSign = dataToSign;
            return this;
        }

        /**
         * Expiry describes the time until which the relying party is ready to
         * wait for the user to confirm the signature request.
         *
         * @param timeToExpiry optional parameter, expressed in milliseconds
         *                     since January 1, 1970, 00:00 UTC. Min value is current time +2
         *                     minutes, max value is current time +30 days. If not present, defaults
         *                     to current time +2 minutes.
         * @return request builder
         */
        public SetOptionalParamsBuilder setExpiry(Long timeToExpiry) {
            this.expiry = timeToExpiry;
            return this;
        }

        /**
         * Push notification will be send to user when transaction is send.
         * Recommended length is 40 characters per title and text.
         *
         * @param pushNotification optional parameter, but if set title and text
         *                         cannot be {@code null} or empty.
         * @return request builder
         */
        public SetOptionalParamsBuilder setPushNotification(PushNotification pushNotification) {
            this.pushNotification = pushNotification;
            return this;
        }

        /**
         * <b>Only relying parties that are integrators should use this
         * method.</b>
         *
         * @param relyingPartyId specifies relying party id for which
         *                       transaction is initiated. It cannot be {@code null} or empty.
         * @return request builder
         */
        public SetOptionalParamsBuilder setRelyingPartyId(String relyingPartyId) {
            this.relyingPartyId = relyingPartyId;
            return this;
        }

        /**
         * <b>OrgIdIssuer is used when requesting an Organisation ID set by
         * another Relying Party</b>
         *
         * @param orgIdIssuer specifies the relying party ID of the
         *                    organisation which issued the organisation ID.
         *
         * @return request builder
         */
        public SetOptionalParamsBuilder setOrgIdIssuer(String orgIdIssuer) {
            this.orgIdIssuer = orgIdIssuer;
            return this;
        }

        public InitiateSignRequest build() {
            return new InitiateSignRequest(userInfoType, userInfo, minRegistrationLevel, title, pushNotification,
                                           expiry, dataToSignType, dataToSign, signatureType, attributesToReturn,
                                           relyingPartyId, orgIdIssuer);
        }

    }

}
