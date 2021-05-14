package com.verisec.frejaeid.client.beans.greencertificate;

public class Recovery extends GreenCertificate{

    /**
     * Returns instance of {@linkplain Recovery} with given certificate value
     *
     * @param certificate Base45-encoded value of the user's green certificate
     */
    public Recovery(String certificate) {
        super(certificate);
    }
}
