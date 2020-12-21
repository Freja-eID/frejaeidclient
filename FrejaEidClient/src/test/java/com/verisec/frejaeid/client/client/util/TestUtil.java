package com.verisec.frejaeid.client.client.util;

import com.verisec.frejaeid.client.beans.general.SslSettings;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;

import java.io.File;
import java.net.URL;

public class TestUtil {

    public static final String CERTIFICATE_PATH = getAbsolutePath("certificates/validServerCertificate.cer");
    public static final String KEYSTORE_PATH = getAbsolutePath("keystores/test.jks");
    public static final String KEYSTORE_PASSWORD = "123123123";
    public static final String INVALID_KEYSTORE_FILE = getAbsolutePath("keystores/invalidKeystoreFile.txt");
    public static final String KEYSTORE_PATH_PKCS12 = getAbsolutePath("keystores/test.p12");
    public static final String KEYSTORE_PATH_JCEKS = getAbsolutePath("keystores/test.jceks");

    public static String getAbsolutePath(String fileName) {
        URL fileUrl = TestUtil.class.getClassLoader().getResource(fileName);
        File file = new File(fileUrl.getFile());
        return file.getAbsolutePath();
    }

    public static SslSettings getDefaultSslSettings() throws FrejaEidClientInternalException {
        return SslSettings.create(KEYSTORE_PATH, KEYSTORE_PASSWORD, CERTIFICATE_PATH);
    }
}
