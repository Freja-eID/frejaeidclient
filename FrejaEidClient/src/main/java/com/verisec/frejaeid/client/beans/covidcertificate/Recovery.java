package com.verisec.frejaeid.client.beans.covidcertificate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Recovery extends CovidCertificate {

    /**
     * Returns instance of {@linkplain Recovery} with given certificate value
     *
     * @param certificate Base45-encoded value of the user's covid certificate
     */
    @JsonCreator
    public Recovery(@JsonProperty(value = "certificate") String certificate) {
        super(certificate);
    }

    @Override
    public String toString() {
        return "Recovery{" + super.toString() + "}";
    }
}
