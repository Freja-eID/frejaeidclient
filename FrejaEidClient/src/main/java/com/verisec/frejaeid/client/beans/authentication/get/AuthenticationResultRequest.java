package com.verisec.frejaeid.client.beans.authentication.get;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.ResultRequest;

public class AuthenticationResultRequest extends ResultRequest {

    /**
     * Returns instance of {@linkplain AuthenticationResultRequest} with given
     * unique reference.
     *
     * @param reference unique transaction reference provided by server as a
     * result of initiating transaction. It cannot be {@code null} or empty.
     * @return request
     */
    public static AuthenticationResultRequest create(String reference) {
        return new AuthenticationResultRequest(reference);
    }

    /**
     * Returns instance of {@linkplain AuthenticationResultRequest} with given
     * unique reference and relying party id.
     *
     * <b>Only relying parties that are integrators should use this method.</b>
     *
     * @param reference unique transaction reference provided by server as a
     * result of initiating transaction. It cannot be {@code null} or empty.
     * @param relyingPartyId specifies relying party id for which transaction is
     * initiated. It cannot be {@code null} or empty.
     * @return request
     */
    public static AuthenticationResultRequest create(String reference, String relyingPartyId) {
        return new AuthenticationResultRequest(reference, relyingPartyId);
    }

    @JsonCreator
    private AuthenticationResultRequest(@JsonProperty(value = "authRef") String reference) {
        super(reference, null);
    }

    private AuthenticationResultRequest(String reference, String relyingPartyId) {
        super(reference, relyingPartyId);
    }

    @JsonProperty(value = "authRef")
    public String getAuthRef() {
        return super.getReference();
    }

    @Override
    public String toString() {
        return "AuthenticationResultRequest{" + super.toString() + '}';
    }
}
