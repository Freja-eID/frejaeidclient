package com.verisec.frejaeid.client.beans.sign.get;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.FrejaHttpResponse;
import java.util.List;
import java.util.Objects;

public class SignResults implements FrejaHttpResponse {

    private final List<SignResult> signatureResults;

    @JsonCreator
    public SignResults(@JsonProperty(value = "signatureResults") List<SignResult> signatureResults) {
        this.signatureResults = signatureResults;
    }

    public List<SignResult> getSignatureResults() {
        return signatureResults;
    }

    @Override
    public int hashCode() {
        return Objects.hash(signatureResults);
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
        final SignResults other = (SignResults) obj;
        if (!Objects.equals(this.signatureResults, other.signatureResults)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SignResults{" + "signatureResults = " + signatureResults + '}';
    }

}
