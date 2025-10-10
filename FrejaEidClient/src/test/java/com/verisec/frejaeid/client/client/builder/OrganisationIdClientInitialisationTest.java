package com.verisec.frejaeid.client.client.builder;

import com.verisec.frejaeid.client.beans.general.SslSettings;
import com.verisec.frejaeid.client.client.api.OrganisationIdClientApi;
import com.verisec.frejaeid.client.client.impl.OrganisationIdClient;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.enums.KeyStoreType;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class OrganisationIdClientInitialisationTest {

    @Test
    public void orgIdClientInit_invalidKeystorePath_expectKeystoreError() {
        try {
            OrganisationIdClient.create(SslSettings.create("x", TestUtil.KEYSTORE_PASSWORD,
                                                           TestUtil.CERTIFICATE_PATH), FrejaEnvironment.TEST).build();
            fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            assertEquals(
                    String.format("Failed to load keystore at path %s with supported keystore types %s.",
                                  "x", KeyStoreType.getAllKeyStoreTypes()),
                    ex.getMessage());
        }
    }

    @Test
    public void orgIdClientInit_invalidCertificatePath_expectKeystoreError() {
        try {
            OrganisationIdClient.create(SslSettings.create(TestUtil.KEYSTORE_PATH, TestUtil.KEYSTORE_PASSWORD, "x"),
                                        FrejaEnvironment.TEST).build();
            fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            assertEquals(
                    String.format("Failed to create trust store with certificate at path %s.",
                                  "x", KeyStoreType.getAllKeyStoreTypes()),
                    ex.getMessage());
        }
    }

    @Test
    public void orgIdClientInit_invalidKeystorePassword_expectKeystoreError() {
        try {
            OrganisationIdClient.create(SslSettings.create(TestUtil.KEYSTORE_PATH, "111111111",
                                                           TestUtil.CERTIFICATE_PATH), FrejaEnvironment.TEST).build();
            fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            assertEquals(
                    String.format("Failed to load keystore at path %s with supported keystore types %s.",
                                  TestUtil.KEYSTORE_PATH, KeyStoreType.getAllKeyStoreTypes()),
                    ex.getMessage());
        }
    }

    @Test
    public void orgIdClientInit_invalidKeystoreFile_expectKeystoreError() {
        try {
            OrganisationIdClient.create(SslSettings.create(TestUtil.INVALID_KEYSTORE_FILE, TestUtil.KEYSTORE_PASSWORD,
                                                           TestUtil.CERTIFICATE_PATH), FrejaEnvironment.TEST).build();
            fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            assertEquals(
                    String.format("Failed to load keystore at path %s with supported keystore types %s.",
                                  TestUtil.INVALID_KEYSTORE_FILE, KeyStoreType.getAllKeyStoreTypes()),
                    ex.getMessage());
        }
    }

    @Test
    public void orgIdClientInit_invalidPollingTime_expectError() {
        try {
            OrganisationIdClient.create(SslSettings.create(TestUtil.KEYSTORE_PATH, TestUtil.KEYSTORE_PASSWORD,
                                                           TestUtil.CERTIFICATE_PATH), FrejaEnvironment.TEST)
                    .setPollingTimeout(500)
                    .build();
            fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Invalid error", "Polling timeout must be between 1 and 60 seconds.",
                                ex.getLocalizedMessage());
        }
    }

    @Test
    public void orgIdClientInit_invalidNullParameter_expectInternalError() {
        try {
            OrganisationIdClient.create(SslSettings.create(TestUtil.KEYSTORE_PATH, TestUtil.KEYSTORE_PASSWORD, null),
                                        FrejaEnvironment.TEST).build();
            fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("KeyStore Path, keyStore password or server certificate path cannot be null or empty.",
                                ex.getLocalizedMessage());
        }
    }

    @Test
    public void orgIdClientInit_invalidEmptyParameter_expectInternalError() {
        try {
            OrganisationIdClient.create(SslSettings.create(TestUtil.KEYSTORE_PATH, TestUtil.KEYSTORE_PASSWORD, " "),
                                        FrejaEnvironment.TEST).build();
            fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("KeyStore Path, keyStore password or server certificate path cannot be null or empty.",
                                ex.getLocalizedMessage());
        }
    }

    @Test
    public void orgIdClientInit_success() throws FrejaEidClientInternalException {
        OrganisationIdClientApi organsiationIdClientJKS = OrganisationIdClient
                .create(SslSettings.create(TestUtil.KEYSTORE_PATH, TestUtil.KEYSTORE_PASSWORD,
                                           TestUtil.CERTIFICATE_PATH), FrejaEnvironment.TEST).build();

        OrganisationIdClientApi organsiationIdClientJCEKS = OrganisationIdClient
                .create(SslSettings.create(TestUtil.KEYSTORE_PATH_JCEKS, TestUtil.KEYSTORE_PASSWORD,
                                           TestUtil.CERTIFICATE_PATH), FrejaEnvironment.TEST).build();

        OrganisationIdClientApi organsiationIdClientPKCS12 = OrganisationIdClient
                .create(SslSettings.create(TestUtil.KEYSTORE_PATH_PKCS12, TestUtil.KEYSTORE_PASSWORD,
                                           TestUtil.CERTIFICATE_PATH), FrejaEnvironment.TEST).build();

        OrganisationIdClientApi organsiationIdClientJKSNoServCert = OrganisationIdClient
                .create(SslSettings.create(TestUtil.KEYSTORE_PATH, TestUtil.KEYSTORE_PASSWORD),
                        FrejaEnvironment.TEST).build();

        OrganisationIdClientApi organsiationIdClientJCEKSNoServCert = OrganisationIdClient
                .create(SslSettings.create(TestUtil.KEYSTORE_PATH_JCEKS, TestUtil.KEYSTORE_PASSWORD),
                        FrejaEnvironment.TEST).build();
    }
}
