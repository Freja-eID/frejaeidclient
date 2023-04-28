package com.verisec.frejaeid.client.beans.custodianship.get;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.RelyingPartyRequest;

import java.util.Objects;

public class GetUserCustodianshipStatusRequest implements RelyingPartyRequest {
    private final String userCountryIdAndCrn;
    @JsonIgnore
    private final String relyingPartyId;

    /**
     * Returns instance of {@linkplain GetUserCustodianshipStatusRequest} for given
     * user Country ID and CRN.
     *
     * @param userCountryIdAndCrn user's country ID and CRN. Must start with country ID
     *                            SE. It cannot be {@code null} or empty.
     * @return request
     */
    public static GetUserCustodianshipStatusRequest create(String userCountryIdAndCrn) {
        return new GetUserCustodianshipStatusRequest(userCountryIdAndCrn);
    }

    /**
     * Returns instance of {@linkplain GetUserCustodianshipStatusRequest} for given
     * userCountryIdAndCrn per relying party.
     *
     * <b>Only relying parties that are integrators should use this method.</b>
     *
     * @param userCountryIdAndCrn user's country ID and CRN. Must start with country ID
     *                            SE. It cannot be {@code null} or empty.
     * @param relyingPartyId specifies relying party id for which transaction is
     *                       initiated. It cannot be {@code null} or empty.
     * @return request
     */
    public static GetUserCustodianshipStatusRequest create(String userCountryIdAndCrn, String relyingPartyId) {
        return new GetUserCustodianshipStatusRequest(userCountryIdAndCrn, relyingPartyId);
    }
    /**
     * Returns instance of builder that is used for creating
     * {@linkplain GetUserCustodianshipStatusRequest} with custom request parameters.
     *
     * @return request builder
     */
    public static GetUserCustodianshipStatusRequestBuilder.SetParamsBuilder createCustom() {
        return new GetUserCustodianshipStatusRequestBuilder.SetParamsBuilder();
    }

    @JsonCreator
    private GetUserCustodianshipStatusRequest(@JsonProperty(value = "userCountryIdAndCrn") String userCountryIdAndCrn) {
        this(userCountryIdAndCrn, null);
    }

    public GetUserCustodianshipStatusRequest(String userCountryIdAndCrn, String relyingPartyId) {
        this.userCountryIdAndCrn = userCountryIdAndCrn;
        this.relyingPartyId = relyingPartyId;
    }

    public String getUserCountryIdAndCrn() { return userCountryIdAndCrn; }

    @JsonIgnore
    public String getRelyingPartyId() { return relyingPartyId; }

    @Override
    public int hashCode() {
        return Objects.hash(userCountryIdAndCrn, relyingPartyId);
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
        final GetUserCustodianshipStatusRequest other = (GetUserCustodianshipStatusRequest) obj;
        if (!Objects.equals(this.userCountryIdAndCrn, other.userCountryIdAndCrn)) {
            return false;
        }
        if (!Objects.equals(this.relyingPartyId, other.relyingPartyId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GetUserCustodianshipStatusRequest{" +
                "userCountryIdAndCrn='" + userCountryIdAndCrn + '\'' +
                ", relyingPartyId='" + relyingPartyId + '\'' +
                '}';
    }
}
