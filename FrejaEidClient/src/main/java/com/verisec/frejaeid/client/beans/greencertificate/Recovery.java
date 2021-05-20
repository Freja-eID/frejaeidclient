package com.verisec.frejaeid.client.beans.greencertificate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Recovery extends GreenCertificate {

    /**
     * Returns instance of {@linkplain Recovery} with given certificate value
     *
     * @param certificate Base45-encoded value of the user's green certificate
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
