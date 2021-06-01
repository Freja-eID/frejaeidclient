package com.verisec.frejaeid.client.beans.covidcertificate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class CovidCertificates {

    private final Vaccines vaccines;
    private final Tests tests;
    private final Recovery recovery;
    private final boolean allowed;

    @JsonCreator
    public CovidCertificates(@JsonProperty(value = "vaccines") Vaccines vaccines,
                             @JsonProperty(value = "tests") Tests tests,
                             @JsonProperty(value = "recovery") Recovery recovery,
                             @JsonProperty(value = "allowed") boolean allowed) {
        this.vaccines = vaccines;
        this.tests = tests;
        this.recovery = recovery;
        this.allowed = allowed;
    }

    public Vaccines getVaccines() {
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
        CovidCertificates that = (CovidCertificates) o;
        return allowed == that.allowed && Objects.equals(vaccines, that.vaccines)
                && Objects.equals(tests, that.tests) && Objects.equals(recovery, that.recovery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vaccines, tests, recovery, allowed);
    }

    @Override
    public String toString() {
        return "CovidCertificates{" +
                "vaccines=" + vaccines +
                ", tests=" + tests +
                ", recovery=" + recovery +
                ", allowed=" + allowed +
                '}';
    }
}
