package com.verisec.frejaeid.client.beans.general;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * @author veiszec
 **/
public class GreenCertificate {

    private final String certificate;
    private final boolean valid;

    @JsonCreator
    public GreenCertificate(@JsonProperty(value = "certificate") String certificate,
                            @JsonProperty(value = "valid") boolean valid) {
        this.certificate = certificate;
        this.valid = valid;
    }

    public String getCertificate() {
        return certificate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GreenCertificate that = (GreenCertificate) o;
        return valid == that.valid && Objects.equals(certificate, that.certificate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(certificate, valid);
    }
}
