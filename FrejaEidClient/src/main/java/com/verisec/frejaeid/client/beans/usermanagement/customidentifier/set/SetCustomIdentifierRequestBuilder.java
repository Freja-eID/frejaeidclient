package com.verisec.frejaeid.client.beans.usermanagement.customidentifier.set;

import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.enums.UserInfoType;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.util.UserInfoUtil;

public class SetCustomIdentifierRequestBuilder {

    public static class SetParamsBuilder {

        /**
         * Sets email as userInfo and {@linkplain UserInfoType#EMAIL} as
         * {@linkplain UserInfoType} for initiating transaction.
         *
         * @param email            will be used to identify the end user. Maximum length is
         *                         256 characters.
         * @param customIdentifier will be set for user. Maximum length is 128
         *                         characters. It cannot be {@code null} or empty.
         * @return request builder
         */
        public SetOptionalParamsBuilder setEmailAndCustomIdentifier(String email, String customIdentifier) {
            return new SetOptionalParamsBuilder(UserInfoType.EMAIL, email, customIdentifier);
        }

        /**
         * Sets ssnUserInfo as userInfo and {@linkplain UserInfoType#SSN} as
         * {@linkplain UserInfoType} for initiating transaction.
         *
         * @param ssnUserInfo      instance of {@linkplain SsnUserInfo} that contains
         *                         personal number and country of user that will be used to identify the
         *                         end user.
         * @param customIdentifier will be set for user. Maximum length is 128
         *                         characters. It cannot be {@code null} or empty.
         * @return request builder
         * @throws FrejaEidClientInternalException if error occurs when
         *                                         generating JSON content from ssnUserInfo
         */
        public SetOptionalParamsBuilder setSsnAndCustomIdentifier(SsnUserInfo ssnUserInfo, String customIdentifier)
                throws FrejaEidClientInternalException {
            return new SetOptionalParamsBuilder(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(ssnUserInfo),
                                                customIdentifier);
        }

        /**
         * Sets phoneNumber as userInfo and {@link UserInfoType#PHONE} as
         * {@linkplain UserInfoType} for initiating transaction.
         *
         * @param phoneNumber      user's phone number that will be used to identify
         *                         the end user in format +467123456789.
         * @param customIdentifier will be set for user. Maximum length is 128
         *                         characters. It cannot be {@code null} or empty.
         * @return request builder
         */
        public SetOptionalParamsBuilder setPhoneNumberAndCustomIdentifier(String phoneNumber, String customIdentifier) {
            return new SetOptionalParamsBuilder(UserInfoType.PHONE, phoneNumber, customIdentifier);
        }

        /**
         * Sets upi as userInfo and {@link UserInfoType#UPI} as
         * {@linkplain UserInfoType} for initiating transaction.
         *
         * @param upi      user's unique personal indentifier that will be used to identify
         *                         the end user.
         * @param customIdentifier will be set for user. Maximum length is 128
         *                         characters. It cannot be {@code null} or empty.
         * @return request builder
         */
        public SetOptionalParamsBuilder setUpiAndCustomIdentifier(String upi, String customIdentifier) {
            return new SetOptionalParamsBuilder(UserInfoType.UPI, upi, customIdentifier);
        }

    }

    public static class SetOptionalParamsBuilder {

        private final UserInfoType userInfoType;
        private final String userInfo;
        private final String customIdentifier;
        private String relyingPartyId = null;

        private SetOptionalParamsBuilder(UserInfoType userInfoType, String userInfo, String customIdentifier) {
            this.userInfoType = userInfoType;
            this.userInfo = userInfo;
            this.customIdentifier = customIdentifier;
        }

        /**
         * <b>Only relying parties that are integrators should use this
         * method.</b>
         *
         * @param relyingPartyId specifies relying party id by which custom
         *                       identifier will be set. It cannot be {@code null} or empty.
         * @return request builder
         */
        public SetOptionalParamsBuilder setRelyingPartyId(String relyingPartyId) {
            this.relyingPartyId = relyingPartyId;
            return this;
        }

        public SetCustomIdentifierRequest build() {
            return new SetCustomIdentifierRequest(userInfoType, userInfo, customIdentifier, relyingPartyId);
        }

    }

}
