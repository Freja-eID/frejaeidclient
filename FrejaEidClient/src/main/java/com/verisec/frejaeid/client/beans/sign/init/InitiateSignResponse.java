package com.verisec.frejaeid.client.beans.sign.init;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.FrejaHttpResponse;

import java.util.Objects;

public class InitiateSignResponse implements FrejaHttpResponse {

    private final String signRef;
    private final String qrCodeSecret;

    @JsonCreator
    public InitiateSignResponse(@JsonProperty(value = "signRef") String signRef,
                                @JsonProperty(value = "qrCodeSecret") String qrCodeSecret) {
        this.signRef = signRef;
        this.qrCodeSecret = qrCodeSecret;
    }

    public String getSignRef() {
        return signRef;
    }

    public String getQrCodeSecret() {
        return qrCodeSecret;
    }

    @Override
    public int hashCode() {
        return Objects.hash(signRef, qrCodeSecret);

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
        if (!Objects.equals(this.qrCodeSecret, other.qrCodeSecret)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InitiateSignResponse{" +
                "signRef='" + signRef + '\'' +
                ", qrCodeSecret='" + qrCodeSecret + '\'' +
                '}';
    }
}
