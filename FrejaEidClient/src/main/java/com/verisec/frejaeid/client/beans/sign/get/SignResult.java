package com.verisec.frejaeid.client.beans.sign.get;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.general.RequestedAttributes;
import com.verisec.frejaeid.client.beans.common.Result;
import com.verisec.frejaeid.client.enums.TransactionStatus;

/**
 * Contains transaction reference, transaction status, details, requested attributes and freja cookie.
 */
public class SignResult extends Result {

    @JsonCreator
    public SignResult(@JsonProperty(value = "signRef") String reference,
                      @JsonProperty(value = "status") TransactionStatus status,
                      @JsonProperty(value = "details") String details,
                      @JsonProperty(value = "requestedAttributes") RequestedAttributes requestedAttributes,
                      @JsonProperty(value = "frejaCookie") String frejaCookie) {
        super(reference, status, details, requestedAttributes, frejaCookie);
    }

    /**
     * @return A unique sign transaction reference.
     */
    @JsonProperty(value = "signRef")
    public String getSignRef() {
        return super.getReference();
    }

    @Override
    public String toString() {
        return "SignResult{" + super.toString() + '}';
    }

}
