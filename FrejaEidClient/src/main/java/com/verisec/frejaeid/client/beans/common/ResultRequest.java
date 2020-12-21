package com.verisec.frejaeid.client.beans.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public abstract class ResultRequest implements RelyingPartyRequest {

    @JsonIgnore
    private final String reference;
    @JsonIgnore
    private final String relyingPartyId;

    public ResultRequest(String reference) {
        this.reference = reference;
        this.relyingPartyId = null;
    }

    public ResultRequest(String reference, String relyingPartyId) {
        this.reference = reference;
        this.relyingPartyId = relyingPartyId;
    }

    public String getReference() {
        return reference;
    }

    @JsonIgnore
    public String getRelyingPartyId() {
        return relyingPartyId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reference, relyingPartyId);
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
        final ResultRequest other = (ResultRequest) obj;
        if (!Objects.equals(this.reference, other.reference)) {
            return false;
        }
        if (!Objects.equals(this.relyingPartyId, other.relyingPartyId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ResultRequest{" + "reference=" + reference + '}';
    }
}
