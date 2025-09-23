package com.verisec.frejaeid.client.beans.authentication.init;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.verisec.frejaeid.client.beans.common.FrejaHttpResponse;

import java.util.Objects;

public class InitiateAuthenticationResponse implements FrejaHttpResponse {

    private final String authRef;
    private final String qrCodeSecret;

    @JsonCreator
    public InitiateAuthenticationResponse(@JsonProperty(value = "authRef") String authRef,
                                          @JsonProperty(value = "qrCodeSecret") String qrCodeSecret) {
        this.authRef = authRef;
        this.qrCodeSecret = qrCodeSecret;
    }

    public String getAuthRef() {
        return authRef;
    }

    public String getQrCodeSecret() {
        return qrCodeSecret;
    }

    @Override
    public int hashCode() {
        return Objects.hash(authRef, qrCodeSecret);
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
        final InitiateAuthenticationResponse other = (InitiateAuthenticationResponse) obj;
        if (!Objects.equals(this.authRef, other.authRef)) {
            return false;
        }
        if (!Objects.equals(this.qrCodeSecret, other.qrCodeSecret)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InitiateAuthenticationResponse{" +
                "authRef='" + authRef + '\'' +
                ", qrCodeSecret='" + qrCodeSecret + '\'' +
                '}';
    }
}
