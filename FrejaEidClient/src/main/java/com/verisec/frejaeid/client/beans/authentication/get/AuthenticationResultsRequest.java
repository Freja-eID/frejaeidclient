package com.verisec.frejaeid.client.beans.authentication.get;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.verisec.frejaeid.client.beans.common.ResultsRequest;

public class AuthenticationResultsRequest extends ResultsRequest {

    /**
     * Returns instance of {@linkplain AuthenticationResultsRequest}.
     *
     * @return request
     */
    public static AuthenticationResultsRequest create() {
        return new AuthenticationResultsRequest(null);
    }

    /**
     * Returns instance of {@linkplain AuthenticationResultsRequest} with given
     * relying party id.
     *
     * <b>Only relying parties that are integrators should use this method.</b>
     *
     * @param relyingPartyId specifies relying party id for which transaction is
     *                       initiated. It cannot be {@code null} or empty.
     * @return request
     */
    public static AuthenticationResultsRequest create(String relyingPartyId) {
        return new AuthenticationResultsRequest(relyingPartyId);
    }

    private AuthenticationResultsRequest(String relyingPartyId) {
        super(relyingPartyId);
    }

    @JsonCreator
    private AuthenticationResultsRequest() {
        super(null);
    }

    @Override
    public String toString() {
        return "AuthenticationResultsRequest{" + super.toString() + '}';
    }
}
