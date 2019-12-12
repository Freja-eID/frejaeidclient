package com.verisec.frejaeid.client.beans.sign.get;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.verisec.frejaeid.client.beans.common.ResultsRequest;

public class SignResultsRequest extends ResultsRequest {

    /**
     * Returns instance of {@linkplain SignResultsRequest}.
     *
     * @return request
     */
    public static SignResultsRequest create() {
        return new SignResultsRequest();
    }

    /**
     * Returns instance of {@linkplain SignResultsRequest} with given relying
     * party id.
     *
     * <b>Only relying parties that are integrators should use this method.</b>
     *
     * @param relyingPartyId specifies relying party id for which transaction is
     * initiated. It cannot be {@code null} or empty.
     * @return request
     */
    public static SignResultsRequest create(String relyingPartyId) {
        return new SignResultsRequest(relyingPartyId);
    }

    private SignResultsRequest(String relyingPartyId) {
        super(relyingPartyId);
    }

    @JsonCreator
    private SignResultsRequest() {
        super(null);
    }

    @Override
    public String toString() {
        return "SignResultsRequest{" + super.toString() + '}';
    }
}
