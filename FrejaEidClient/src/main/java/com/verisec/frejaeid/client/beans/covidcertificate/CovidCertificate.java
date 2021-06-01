package com.verisec.frejaeid.client.beans.covidcertificate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public abstract class CovidCertificate {

    private final String certificate;

    @JsonCreator
    public CovidCertificate(@JsonProperty(value = "certificate") String certificate) {
        this.certificate = certificate;
    }

    public String getCertificate() {
        return certificate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CovidCertificate that = (CovidCertificate) o;
        return Objects.equals(certificate, that.certificate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(certificate);
    }

    @Override
    public String toString() {
        return "CovidCertificate{" +
                "certificate='" + certificate + '\'' +
                '}';
    }
}
