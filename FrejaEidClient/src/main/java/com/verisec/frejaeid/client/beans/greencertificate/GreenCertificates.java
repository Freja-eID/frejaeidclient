package com.verisec.frejaeid.client.beans.greencertificate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GreenCertificates {

    private final Vaccines vaccines;
    private final Tests tests;
    private final Recovery recovery;
    private final boolean allowed;

    @JsonCreator
    public GreenCertificates(@JsonProperty(value = "vaccines") Vaccines vaccines,
                             @JsonProperty(value = "tests") Tests tests,
                             @JsonProperty(value = "recovery") Recovery recovery,
                             @JsonProperty(value = "allowed") boolean allowed) {
        this.vaccines = vaccines;
        this.tests = tests;
        this.recovery = recovery;
        this.allowed = allowed;
    }

    public Vaccines getVaccine() {
        return vaccines;
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
        return allowed == that.allowed && Objects.equals(vaccines, that.vaccines)
                && Objects.equals(tests, that.tests) && Objects.equals(recovery, that.recovery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vaccines, tests, recovery, allowed);
    }

    @Override
    public String toString() {
        return "GreenCertificates{" +
                "vaccines=" + vaccines +
                ", tests=" + tests +
                ", recovery=" + recovery +
                ", allowed=" + allowed +
                '}';
    }
}
