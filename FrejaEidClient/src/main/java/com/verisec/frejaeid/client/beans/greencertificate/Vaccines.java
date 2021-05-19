package com.verisec.frejaeid.client.beans.greencertificate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Vaccines extends GreenCertificate {

    /**
     * Returns instance of {@linkplain Vaccines} with given certificate value
     *
     * @param certificate Base45-encoded value of the user's green certificate
     */
    @JsonCreator
    public Vaccines(@JsonProperty(value = "certificate") String certificate) {
        super(certificate);
    }

    @Override
    public String toString() {
        return "Vaccines{" + super.toString() + "}";
    }
}
