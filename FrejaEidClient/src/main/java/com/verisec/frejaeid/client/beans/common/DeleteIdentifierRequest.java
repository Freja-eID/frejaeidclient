package com.verisec.frejaeid.client.beans.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;

/**
 *
 * @author vemijel
 */
public abstract class DeleteIdentifierRequest implements RelyingPartyRequest {

    @JsonIgnore
    private final String identifier;
    @JsonIgnore
    private final String relyingPartyId;

    public DeleteIdentifierRequest(String identifier) {
        this.identifier = identifier;
        this.relyingPartyId = null;
    }

    public DeleteIdentifierRequest(String identifier, String relyingPartyId) {
        this.identifier = identifier;
        this.relyingPartyId = relyingPartyId;
    }

    protected String getIdentifier() {
        return identifier;
    }

    public String getRelyingPartyId() {
        return relyingPartyId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, relyingPartyId);
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
        final DeleteIdentifierRequest other = (DeleteIdentifierRequest) obj;
        if (!Objects.equals(this.identifier, other.identifier)) {
            return false;
        }
        if (!Objects.equals(this.relyingPartyId, other.relyingPartyId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DeleteIdentifierRequest{" + "identifier=" + identifier + '}';
    }

}
