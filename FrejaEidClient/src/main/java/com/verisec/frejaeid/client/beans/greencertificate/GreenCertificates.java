package com.verisec.frejaeid.client.beans.greencertificate;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GreenCertificates {

    private final Vaccine vaccine;
    private final Tests tests;
    private final Recovery recovery;
    private final boolean allowed;

    public GreenCertificates(@JsonProperty(value = "vaccine") Vaccine vaccine,
                             @JsonProperty(value = "tests") Tests tests,
                             @JsonProperty(value = "recovery") Recovery recovery,
                             @JsonProperty(value = "allowed") boolean allowed) {
        this.vaccine = vaccine;
        this.tests = tests;
        this.recovery = recovery;
        this.allowed = allowed;
    }

    public Vaccine getVaccine() {
        return vaccine;
    }

    public Tests getTests() {
        return tests;
    }

    public Recovery getRecovery() {
        return recovery;
    }

    public boolean isAllowed() {
        return allowed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GreenCertificates that = (GreenCertificates) o;
        return allowed == that.allowed && Objects.equals(vaccine, that.vaccine)
                && Objects.equals(tests, that.tests) && Objects.equals(recovery, that.recovery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vaccine, tests, recovery, allowed);
    }

    @Override
    public String toString() {
        return "GreenCertificates{" +
                "vaccine=" + vaccine +
                ", tests=" + tests +
                ", recovery=" + recovery +
                ", allowed=" + allowed +
                '}';
    }
}
