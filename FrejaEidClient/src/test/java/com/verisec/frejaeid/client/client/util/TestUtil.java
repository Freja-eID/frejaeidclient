package com.verisec.frejaeid.client.client.util;

import java.io.File;
import java.net.URL;

public class TestUtil {

    public static String CERTIFICATE_PATH = "certificates/validServerCertificate.cer";
    public static String KEYSTORE_PATH = "keystores/test.jks";
    public static String KEYSTORE_PASSWORD = "123123123";
    public static String INVALID_KEYSTORE_FILE = "keystores/invalidKeystoreFile.txt";
    public static String KEYSTORE_PATH_PKCS12 = "keystores/test.p12";
    public static String KEYSTORE_PATH_JCEKS = "keystores/test.jceks";

    public static String getKeystorePath(String fileName) {
        URL fileUrl = TestUtil.class.getClassLoader().getResource(fileName);
        File file = new File(fileUrl.getFile());;
        return file.getAbsolutePath();
    }
}
