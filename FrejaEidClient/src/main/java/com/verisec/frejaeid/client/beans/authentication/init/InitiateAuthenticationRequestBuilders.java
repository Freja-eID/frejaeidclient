package com.verisec.frejaeid.client.beans.authentication.init;

import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.enums.AttributeToReturn;
import com.verisec.frejaeid.client.enums.MinRegistrationLevel;
import com.verisec.frejaeid.client.enums.TransactionContext;
import com.verisec.frejaeid.client.enums.UserInfoType;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.util.UserInfoUtil;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class InitiateAuthenticationRequestBuilders {

    public static class UserInfoBuilder {

        /**
         * Sets email as userInfo and {@linkplain UserInfoType#EMAIL} as
         * {@linkplain UserInfoType} for initiating transaction.
         *
         * @param email user's email that will be used to identify the end user.
         * Maximum length is 256 characters.
         * @return request builder
         */
        public SetOptionalParamsBuilder setEmail(String email) {
            return new SetOptionalParamsBuilder(UserInfoType.EMAIL, email);
        }

        /**
         * Sets ssnUserInfo as userInfo and {@linkplain UserInfoType#SSN} as
         * {@linkplain UserInfoType} for initiating transaction.
         *
         * @param ssnUserInfo instance {@linkplain SsnUserInfo} that contains
         * personal number and country of user that will be used to identify the
         * end user.
         * @return request builder
         * @throws FrejaEidClientInternalException if error occurs when
         * generating JSON content from ssnUserInfo
         */
        public SetOptionalParamsBuilder setSsn(SsnUserInfo ssnUserInfo) throws FrejaEidClientInternalException {
            return new SetOptionalParamsBuilder(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(ssnUserInfo));
        }

        /**
         * Sets phoneNumber as userInfo and {@link UserInfoType#PHONE} as
         * {@linkplain UserInfoType} for initiating transaction.
         *
         * @param phoneNumber user's phone number that will be used to identify
         * the end user in format +467123456789.
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
         * organisation
         *
         * @return request builder
         */
        public SetOptionalParamsBuilder setOrganisationId(String identifier) {
            return new SetOptionalParamsBuilder(UserInfoType.ORG_ID, identifier);
        }

    }

    public static class SetOptionalParamsBuilder {

        private final UserInfoType userInfoType;
        private final String userInfo;
        private MinRegistrationLevel minRegistrationLevel = MinRegistrationLevel.BASIC;
        private Set<AttributeToReturn> attributesToReturn = null;
        private String relyingPartyId = null;

        private SetOptionalParamsBuilder(UserInfoType userInfoType, String userInfo) {
            this.userInfoType = userInfoType;
            this.userInfo = userInfo;
        }

        /**
         * Minimum registration level is only used with
         * {@link TransactionContext#PERSONAL}. It is optional, default value is
         * {@link MinRegistrationLevel#BASIC}.
         *
         * @param minRegistrationLevel will be set as minimum required
         * registration level for user in order to approve/decline
         * authentication.
         * @return request builder
         */
        public SetOptionalParamsBuilder setMinRegistrationLevel(MinRegistrationLevel minRegistrationLevel) {
            if (minRegistrationLevel == null) {
                minRegistrationLevel = MinRegistrationLevel.BASIC;
            }
            this.minRegistrationLevel = minRegistrationLevel;
            return this;
        }

        /**
         * Attributes to return are attributes that are requested from user to
         * share.
         *
         * @param attributesToReturn can be any value from
         * {@linkplain AttributeToReturn}
         * @return request builder
         */
        public SetOptionalParamsBuilder setAttributesToReturn(AttributeToReturn... attributesToReturn) {
            this.attributesToReturn = new TreeSet();
            this.attributesToReturn.addAll(Arrays.asList(attributesToReturn));
            return this;
        }

        /**
         * <b>Only relying parties that are integrators should use this
         * method.</b>
         *
         * @param relyingPartyId specifies relying party id for which
         * transaction is initiated. It cannot be {@code null} or empty.
         * @return request builder
         */
        public SetOptionalParamsBuilder setRelyingPartyId(String relyingPartyId) {
            this.relyingPartyId = relyingPartyId;
            return this;
        }

        public InitiateAuthenticationRequest build() {
            return new InitiateAuthenticationRequest(userInfoType, userInfo, minRegistrationLevel, attributesToReturn, relyingPartyId);
        }

    }

}
