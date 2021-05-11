package com.verisec.frejaeid.client.beans.greencertificate;

public class Vaccines extends GreenCertificate{

    /**
     * Returns instance of {@linkplain Vaccines} with given certificate value
     *
     * @param certificate Base45-encoded value of the user's green certificate
     */
    public Vaccines(String certificate) {
        super(certificate);
    }
}
