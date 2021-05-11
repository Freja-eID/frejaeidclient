package com.verisec.frejaeid.client.beans.greencertificate;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public abstract class GreenCertificate {

    private final String certificate;

    public GreenCertificate(@JsonProperty(value = "certificate") String certificate) {
        this.certificate = certificate;
    }

    public String getCertificate() {
        return certificate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GreenCertificate that = (GreenCertificate) o;
        return Objects.equals(certificate, that.certificate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(certificate);
    }

    @Override
    public String toString() {
        return "GreenCertificate{" +
                "certificate='" + certificate + '\'' +
                '}';
    }
}
