package com.verisec.frejaeid.client.beans.custodianship.get;

public class GetUserCustodianshipStatusRequestBuilder {
    public static class SetParamsBuilder {

        /**
         * Sets country code ID and CRN of user
         *
         * @param countryIdAndCrn            will be used to identify the end user. Must start with country
         *                                   code "SE"
         * @return request builder
         */
        public GetUserCustodianshipStatusRequestBuilder.SetOptionalParamsBuilder setUserCountryIdAndCrn(
                String countryIdAndCrn) {
            return new GetUserCustodianshipStatusRequestBuilder.SetOptionalParamsBuilder(countryIdAndCrn);
        }

    }

    public static class SetOptionalParamsBuilder {

        private final String countryIdAndCrn;
        private String relyingPartyId = null;

        private SetOptionalParamsBuilder(String countryIdAndCrn) {
            this.countryIdAndCrn = countryIdAndCrn;
        }

        /**
         * <b>Only relying parties that are integrators should use this
         * method.</b>
         *
         * @param relyingPartyId specifies relying party id by which you are requesting
         *                       the user's custodianship status. It cannot be {@code null} or empty
         * @return request builder
         */
        public GetUserCustodianshipStatusRequestBuilder.SetOptionalParamsBuilder setRelyingPartyId(String relyingPartyId) {
            this.relyingPartyId = relyingPartyId;
            return this;
        }

        public GetUserCustodianshipStatusRequest build() {
            return new GetUserCustodianshipStatusRequest(countryIdAndCrn, relyingPartyId);
        }

    }
}
