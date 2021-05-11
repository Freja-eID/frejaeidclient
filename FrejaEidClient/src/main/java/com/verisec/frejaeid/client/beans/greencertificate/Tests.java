package com.verisec.frejaeid.client.beans.greencertificate;

public class Tests extends GreenCertificate {

    /**
     * Returns instance of {@linkplain Tests} with given certificate value
     *
     * @param certificate Base45-encoded value of the user's green certificate
     */
    public Tests(String certificate) {
        super(certificate);
    }
}
