package com.verisec.frejaeid.client.beans.sign.init;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.FrejaHttpResponse;
import java.util.Objects;

public class InitiateSignResponse implements FrejaHttpResponse {

    private final String signRef;

    @JsonCreator
    public InitiateSignResponse(@JsonProperty(value = "signRef") String signRef) {
        this.signRef = signRef;
    }

    public String getSignRef() {
        return signRef;
    }

    @Override
    public int hashCode() {
        return Objects.hash(signRef);

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
        final InitiateSignResponse other = (InitiateSignResponse) obj;
        if (!Objects.equals(this.signRef, other.signRef)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InitiateSignResponse{" + "signRef=" + signRef + '}';
    }
}
