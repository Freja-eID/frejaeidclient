package com.verisec.frejaeid.client.beans.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.general.RequestedAttributes;
import com.verisec.frejaeid.client.enums.TransactionStatus;

import java.util.Objects;

public abstract class Result implements FrejaHttpResponse {

    @JsonIgnore
    private final String reference;
    private final TransactionStatus status;
    private final String details;
    private final RequestedAttributes requestedAttributes;

    @JsonCreator
    public Result(String reference,
                  @JsonProperty(value = "status") TransactionStatus status,
                  @JsonProperty(value = "details") String details,
                  @JsonProperty(value = "requestedAttributes") RequestedAttributes requestedAttributes) {
        this.reference = reference;
        this.status = status;
        this.details = details;
        this.requestedAttributes = requestedAttributes;
    }

    public String getReference() {
        return reference;
    }

    /**
     * @return one of {@linkplain TransactionStatus} for transaction which
     * reference was sent in related request
     */
    public TransactionStatus getStatus() {
        return status;
    }

    /**
     * JWS in compact serialized form as following: BASE64URL(UTF8(JWS Protected
     * Header)) . BASE64URL(JWS Payload) . BASE64URL(JWS Signature)
     *
     * @return details if transaction is approved otherwise {@code null}
     */
    public String getDetails() {
        return details;
    }

    /**
     * Provides additional attributes about a user if requested. In case of
     * organisation id transactions this will always be {@code null}.
     *
     * @return instance of {@linkplain RequestedAttributes}
     */
    public RequestedAttributes getRequestedAttributes() {
        return requestedAttributes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reference, status, details, requestedAttributes);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Result other = (Result) obj;
        if (!Objects.equals(this.reference, other.reference)) {
            return false;
        }
        if (this.status != other.status) {
            return false;
        }
        if (!Objects.equals(this.details, other.details)) {
            return false;
        }
        return Objects.equals(this.requestedAttributes, other.requestedAttributes);
    }

    @Override
    public String toString() {
        return "Result{" + "reference=" + reference + ", status=" + status + ", details=" + details + ", " +
                "requestedAttributes=" + requestedAttributes + '}';
    }

}
