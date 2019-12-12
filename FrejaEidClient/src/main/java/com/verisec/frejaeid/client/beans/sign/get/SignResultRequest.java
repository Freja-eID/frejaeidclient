package com.verisec.frejaeid.client.beans.sign.get;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.ResultRequest;

public class SignResultRequest extends ResultRequest {

    /**
     * Returns instance of {@linkplain SignResultRequest} with given unique
     * reference.
     *
     * @param reference unique transaction reference provided by server as a
     * result of initiating transaction. It cannot be {@code null} or empty.
     * @return request
     */
    public static SignResultRequest create(String reference) {
        return new SignResultRequest(reference);
    }

    /**
     * Returns instance of {@linkplain SignResultRequest} with given unique
     * reference and relying party id.
     *
     * <b>Only relying parties that are integrators should use this method.</b>
     *
     * @param reference unique transaction reference provided by server as a
     * result of initiating transaction. It cannot be {@code null} or empty.
     * @param relyingPartyId specifies relying party id for which transaction is
     * initiated. It cannot be {@code null} or empty.
     * @return request
     */
    public static SignResultRequest create(String reference, String relyingPartyId) {
        return new SignResultRequest(reference, relyingPartyId);
    }

    @JsonCreator
    private SignResultRequest(@JsonProperty(value = "signRef") String reference) {
        super(reference);
    }

    private SignResultRequest(String reference, String relyingPartyId) {
        super(reference, relyingPartyId);
    }

    @JsonProperty(value = "signRef")
    public String getSignRef() {
        return super.getReference();
    }

    @Override
    public String toString() {
        return "SignResultRequest{" + super.toString() + '}';
    }
}
