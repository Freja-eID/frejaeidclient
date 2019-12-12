package com.verisec.frejaeid.client.beans.sign.cancel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.CancelRequest;

public class CancelSignRequest extends CancelRequest {

    /**
     * Returns instance of {@linkplain CancelSignRequest} with given unique
     * reference.
     *
     * @param reference unique transaction reference provided by server as a
     * result of initiating transaction. It cannot be {@code null} or empty.
     * @return request
     */
    public static CancelSignRequest create(String reference) {
        return new CancelSignRequest(reference);
    }

    /**
     * Returns instance of {@linkplain CancelSignRequest} with given unique
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
    public static CancelSignRequest create(String reference, String relyingPartyId) {
        return new CancelSignRequest(reference, relyingPartyId);
    }

    @JsonCreator
    private CancelSignRequest(@JsonProperty(value = "signRef") String reference) {
        super(reference);
    }

    private CancelSignRequest(String reference, String relyingPartyId) {
        super(reference, relyingPartyId);
    }

    @JsonProperty(value = "signRef")
    public String getSignRef() {
        return super.getReference();
    }

    @Override
    public String toString() {
        return "CancelSignRequest{" + super.toString() + '}';
    }

}
