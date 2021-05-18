package com.verisec.frejaeid.client.beans.greencertificate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Tests extends GreenCertificate {

    /**
     * Returns instance of {@linkplain Tests} with given certificate value
     *
     * @param certificate Base45-encoded value of the user's green certificate
     */
    @JsonCreator
    public Tests(@JsonProperty(value = "certificate") String certificate) {
        super(certificate);
    }
}
