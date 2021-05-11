package com.verisec.frejaeid.client.beans.greencertificate;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Recovery {

    private final String certificate;

    public Recovery(@JsonProperty(value = "certificate") String certificate) {
        this.certificate = certificate;
    }

    public String getCertificate() {
        return certificate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recovery recovery = (Recovery) o;
        return Objects.equals(certificate, recovery.certificate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(certificate);
    }

    @Override
    public String toString() {
        return "Recovery{" +
                "certificate='" + certificate + '\'' +
                '}';
    }
}
