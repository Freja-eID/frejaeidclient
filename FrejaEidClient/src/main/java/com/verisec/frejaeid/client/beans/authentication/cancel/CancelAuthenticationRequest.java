package com.verisec.frejaeid.client.beans.authentication.cancel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.CancelRequest;

public class CancelAuthenticationRequest extends CancelRequest {

    /**
     * Returns instance of {@linkplain CancelAuthenticationRequest} with given
     * unique reference.
     *
     * @param reference unique transaction reference provided by server as a
     *                  result of initiating transaction. It cannot be {@code null} or empty.
     * @return request
     */
    public static CancelAuthenticationRequest create(String reference) {
        return new CancelAuthenticationRequest(reference);
    }

    /**
     * Returns instance of {@linkplain CancelAuthenticationRequest} with given
     * unique reference and relying party id.
     *
     * <b>Only relying parties that are integrators should use this method.</b>
     *
     * @param reference      unique transaction reference provided by server as a
     *                       result of initiating transaction. It cannot be {@code null} or empty.
     * @param relyingPartyId specifies relying party id for which transaction is
     *                       initiated. It cannot be {@code null} or empty.
     * @return request
     */
    public static CancelAuthenticationRequest create(String reference, String relyingPartyId) {
        return new CancelAuthenticationRequest(reference, relyingPartyId);
    }

    @JsonCreator
    private CancelAuthenticationRequest(@JsonProperty(value = "authRef") String reference) {
        super(reference);
    }

    private CancelAuthenticationRequest(String reference, String relyingPartyId) {
        super(reference, relyingPartyId);
    }

    @JsonProperty(value = "authRef")
    public String getAuthRef() {
        return super.getReference();
    }

    @Override
    public String toString() {
        return "CancelAuthenticationRequest{" + super.toString() + '}';
    }

}
