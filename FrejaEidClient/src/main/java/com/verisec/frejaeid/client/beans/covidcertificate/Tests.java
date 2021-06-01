package com.verisec.frejaeid.client.beans.covidcertificate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Tests extends CovidCertificate {

    /**
     * Returns instance of {@linkplain Tests} with given certificate value
     *
     * @param certificate Base45-encoded value of the user's covid certificate
     */
    @JsonCreator
    public Tests(@JsonProperty(value = "certificate") String certificate) {
        super(certificate);
    }

    @Override
    public String toString() {
        return "Tests{" + super.toString() + "}";
    }
}
