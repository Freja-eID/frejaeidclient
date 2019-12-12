package com.verisec.frejaeid.client.beans.organisationid.init;

import com.verisec.frejaeid.client.beans.general.OrganisationId;
import com.verisec.frejaeid.client.beans.general.SsnUserInfo;
import com.verisec.frejaeid.client.enums.MinRegistrationLevel;
import com.verisec.frejaeid.client.enums.UserInfoType;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.util.UserInfoUtil;

public class InitiateAddOrganisationIdRequestBuilders {

    public static class SetParamsBuilder {

        /**
         * Sets email as userInfo and {@linkplain UserInfoType#EMAIL} as
         * {@linkplain UserInfoType} for initiating transaction.
         *
         * @param email user's email that will be used to identify the end user.
         * Maximum length is 256 characters.
         * @param organisationId instance of {@linkplain OrganisationId} that
         * contains information regarding organisation id.
         * @return request builder
         */
        public SetOptionalParamsBuilder setEmailAndOrganisationId(String email, OrganisationId organisationId) {
            return new SetOptionalParamsBuilder(UserInfoType.EMAIL, email, organisationId);
        }

        /**
         * Sets ssnUserInfo as userInfo and {@linkplain UserInfoType#SSN} as
         * {@linkplain UserInfoType} for initiating transaction.
         *
         * @param ssnUserInfo instance of {@linkplain SsnUserInfo} that contains
         * personal number and country of user that will be used to identify the
         * end user.
         * @param organisationId instance of {@linkplain OrganisationId} that
         * contains information regarding organisation id.
         *
         * @return request builder
         * @throws FrejaEidClientInternalException if error occurs when
         * generating JSON content from ssnUserInfo
         */
        public SetOptionalParamsBuilder setSsnAndOrganisationId(SsnUserInfo ssnUserInfo, OrganisationId organisationId) throws FrejaEidClientInternalException {
            return new SetOptionalParamsBuilder(UserInfoType.SSN, UserInfoUtil.convertSsnUserInfo(ssnUserInfo), organisationId);
        }

        /**
         * Sets phoneNumber as userInfo and {@link UserInfoType#PHONE} as
         * {@linkplain UserInfoType} for initiating transaction.
         *
         * @param phoneNumber user's phone number that will be used to identify
         * the end user in format +467123456789.
         * @param organisationId instance of {@linkplain OrganisationId} that
         * contains information regarding organisation id.
         *
         * @return request builder
         */
        public SetOptionalParamsBuilder setPhoneNumberAndOrganisationId(String phoneNumber, OrganisationId organisationId) {
            return new SetOptionalParamsBuilder(UserInfoType.PHONE, phoneNumber, organisationId);
        }

        /**
         * Sets {@linkplain UserInfoType#INFERRED} as {@linkplain UserInfoType}
         * for initiating transaction.
         *
         *
         * @param organisationId instance of {@linkplain OrganisationId} that
         * contains information regarding organisation id.
         * @return request builder
         */
        public SetOptionalParamsBuilder setInferredAndOrganisationId(OrganisationId organisationId) {
            return new SetOptionalParamsBuilder(UserInfoType.INFERRED, "N/A", organisationId);
        }

    }

    public static class SetOptionalParamsBuilder {

        private final UserInfoType userInfoType;
        private final String userInfo;
        private final OrganisationId organisationId;
        private MinRegistrationLevel minRegistrationLevel = MinRegistrationLevel.EXTENDED;
        private Long expiry = null;
        private String relyingPartyId = null;

        private SetOptionalParamsBuilder(UserInfoType userInfoType, String userInfo, OrganisationId organisationId) {
            this.userInfoType = userInfoType;
            this.userInfo = userInfo;
            this.organisationId = organisationId;
        }

        /**
         * Minimum registration level is optional, default value is
         * {@link MinRegistrationLevel#EXTENDED}.
         *
         * @param minRegistrationLevel will be set as minimum required
         * registration level for user in order to approve/decline adding
         * organisation id.
         * @return request builder
         */
        public SetOptionalParamsBuilder setMinRegistrationLevel(MinRegistrationLevel minRegistrationLevel) {
            if (minRegistrationLevel == null) {
                minRegistrationLevel = MinRegistrationLevel.EXTENDED;
            }
            this.minRegistrationLevel = minRegistrationLevel;
            return this;
        }

        /**
         * Expiry describes the time until which the relying party is ready to
         * wait for the user to confirm the adding organisation id request.
         *
         * @param timeToExpiry optional parameter, expressed in milliseconds
         * since January 1, 1970, 00:00 UTC. Min value is current time +2
         * minutes, max value is current time +30 days. If not present, defaults
         * to current time 7 days.
         * @return request builder
         */
        public SetOptionalParamsBuilder setExpiry(Long timeToExpiry) {
            this.expiry = timeToExpiry;
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

        public InitiateAddOrganisationIdRequest build() {
            return new InitiateAddOrganisationIdRequest(userInfoType, userInfo, organisationId, minRegistrationLevel, expiry, relyingPartyId);
        }

    }
}
