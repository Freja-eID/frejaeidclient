package com.verisec.frejaeid.client.beans.greencertificate;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Vaccine {

    private final String certificate;

    public Vaccine(@JsonProperty(value = "certificate") String certificate) {
        this.certificate = certificate;
    }


    public String getCertificate() {
        return certificate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vaccine vaccine = (Vaccine) o;
        return Objects.equals(certificate, vaccine.certificate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(certificate);
    }

    @Override
    public String toString() {
        return "Vaccine{" +
                "certificate='" + certificate + '\'' +
                '}';
    }
}
