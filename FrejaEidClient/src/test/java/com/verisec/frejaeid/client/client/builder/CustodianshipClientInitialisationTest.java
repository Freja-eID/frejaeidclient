package com.verisec.frejaeid.client.client.builder;

import com.verisec.frejaeid.client.beans.general.SslSettings;
import com.verisec.frejaeid.client.client.api.CustodianshipClientApi;
import com.verisec.frejaeid.client.client.impl.CustodianshipClient;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import org.junit.Assert;
import org.junit.Test;

public class CustodianshipClientInitialisationTest {
    @Test
    public void custodianshipClientInit_invalidKeystorePath_expectKeystoreError() {
        try {
            CustodianshipClient.create(SslSettings.create("x", TestUtil.KEYSTORE_PASSWORD,
                                                          TestUtil.CERTIFICATE_PATH), FrejaEnvironment.TEST).build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Failed to initiate SSL context with supported keystore types JKS, JCEKS and PKCS12.",
                                ex.getLocalizedMessage());
        }
    }

    @Test
    public void custodianshipClientInit_invalidCertificatePath_expectKeystoreError() {
        try {
            CustodianshipClient.create(SslSettings.create(TestUtil.KEYSTORE_PATH, TestUtil.KEYSTORE_PASSWORD, "x"),
                                          FrejaEnvironment.TEST).build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Failed to initiate SSL context with supported keystore types JKS, JCEKS and PKCS12.",
                                ex.getLocalizedMessage());
        }
    }

    @Test
    public void custodianshipClientInit_invalidKeystorePassword_expectKeystoreError() {
        try {
            CustodianshipClient.create(SslSettings.create(TestUtil.KEYSTORE_PATH, "111111111",
                                                             TestUtil.CERTIFICATE_PATH), FrejaEnvironment.TEST).build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Failed to initiate SSL context with supported keystore types JKS, JCEKS and PKCS12.",
                                ex.getLocalizedMessage());
        }
    }

    @Test
    public void custodianshipClientInit_invalidKeystoreFile_expectKeystoreError() {
        try {
            CustodianshipClient.create(SslSettings.create(TestUtil.INVALID_KEYSTORE_FILE,
                                                             TestUtil.KEYSTORE_PASSWORD, TestUtil.CERTIFICATE_PATH),
                                          FrejaEnvironment.TEST).build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Failed to initiate SSL context with supported keystore types JKS, JCEKS and PKCS12.",
                                ex.getLocalizedMessage());
        }
    }

    @Test
    public void custodianshipClientInit_invalidPollingTime_expectError() {
        try {
            CustodianshipClient.create(SslSettings.create(TestUtil.KEYSTORE_PATH, TestUtil.KEYSTORE_PASSWORD,
                                                             TestUtil.CERTIFICATE_PATH), FrejaEnvironment.TEST)
                    .setPollingTimeout(500)
                    .build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Polling timeout must be between 1 and 60 seconds.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void custodianshipClientInit_invalidNullParameter_expectInternalError() {
        try {
            CustodianshipClient.create(SslSettings.create(null, TestUtil.KEYSTORE_PASSWORD,
                                                             TestUtil.CERTIFICATE_PATH), FrejaEnvironment.TEST).build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("KeyStore Path, keyStore password or server certificate path cannot be null or empty.",
                                ex.getLocalizedMessage());
        }
    }

    @Test
    public void custodianshipClientInit_invalidEmptyParameter_expectInternalError() {
        try {
            CustodianshipClient.create(SslSettings.create(" ", TestUtil.KEYSTORE_PASSWORD,
                                                             TestUtil.CERTIFICATE_PATH), FrejaEnvironment.TEST).build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("KeyStore Path, keyStore password or server certificate path cannot be null or empty.",
                                ex.getLocalizedMessage());
        }
    }

    @Test
    public void custodianshipClientInit_success() throws FrejaEidClientInternalException {
        CustodianshipClientApi custodianshipClientJKS = CustodianshipClient
                .create(SslSettings.create(TestUtil.KEYSTORE_PATH, TestUtil.KEYSTORE_PASSWORD,
                                           TestUtil.CERTIFICATE_PATH), FrejaEnvironment.TEST).build();

        CustodianshipClientApi custodianshipClientJCEKS = CustodianshipClient
                .create(SslSettings.create(TestUtil.KEYSTORE_PATH_JCEKS,
                                           TestUtil.KEYSTORE_PASSWORD,
                                           TestUtil.CERTIFICATE_PATH), FrejaEnvironment.TEST).build();

        CustodianshipClientApi custodianshipClientPKCS12 = CustodianshipClient
                .create(SslSettings.create(TestUtil.KEYSTORE_PATH_PKCS12,
                                           TestUtil.KEYSTORE_PASSWORD,
                                           TestUtil.CERTIFICATE_PATH), FrejaEnvironment.TEST).build();

        CustodianshipClientApi custodianshipClientJKSNoServCert = CustodianshipClient
                .create(SslSettings.create(TestUtil.KEYSTORE_PATH, TestUtil.KEYSTORE_PASSWORD),
                        FrejaEnvironment.TEST).build();

        CustodianshipClientApi custodianshipClientJCEKSNoServCert = CustodianshipClient
                .create(SslSettings.create(TestUtil.KEYSTORE_PATH_JCEKS, TestUtil.KEYSTORE_PASSWORD),
                        FrejaEnvironment.TEST).build();
    }
}
