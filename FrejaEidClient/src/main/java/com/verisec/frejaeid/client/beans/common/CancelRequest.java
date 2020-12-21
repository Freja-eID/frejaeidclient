package com.verisec.frejaeid.client.beans.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public abstract class CancelRequest implements RelyingPartyRequest {

    @JsonIgnore
    private final String reference;
    @JsonIgnore
    private final String relyingPartyId;

    public CancelRequest(String reference) {
        this.reference = reference;
        this.relyingPartyId = null;
    }

    public CancelRequest(String reference, String relyingPartyId) {
        this.reference = reference;
        this.relyingPartyId = relyingPartyId;
    }

    public String getReference() {
        return reference;
    }

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
        final CancelRequest other = (CancelRequest) obj;
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
        return "CancelRequest{" + "reference=" + reference + '}';
    }

}
