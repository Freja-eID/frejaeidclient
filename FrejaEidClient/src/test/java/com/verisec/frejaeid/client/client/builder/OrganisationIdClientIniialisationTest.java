package com.verisec.frejaeid.client.client.builder;

import com.verisec.frejaeid.client.beans.general.SslSettings;
import com.verisec.frejaeid.client.client.api.OrganisationIdClientApi;
import com.verisec.frejaeid.client.client.impl.OrganisationIdClient;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import org.junit.Assert;
import org.junit.Test;

public class OrganisationIdClientIniialisationTest {

    @Test
    public void orgIdClientInit_invalidKeystorePath_expectKeystoreError() {
        try {
            OrganisationIdClientApi organsiationIdClient = OrganisationIdClient.create(SslSettings.create("x", TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                    .build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Invalid error", "Failed to initiate SSL context with supported keystore types JKS, JCEKS and PKCS12.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void orgIdClientInit_invalidCertificatePath_expectKeystoreError() {
        try {
            OrganisationIdClientApi organsiationIdClient = OrganisationIdClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, "x"), FrejaEnvironment.TEST)
                    .build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Invalid error", "Failed to initiate SSL context with supported keystore types JKS, JCEKS and PKCS12.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void orgIdClientInit_invalidKeystorePassword_expectKeystoreError() {
        try {
            OrganisationIdClientApi organsiationIdClient = OrganisationIdClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), "111111111", TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                    .build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Invalid error", "Failed to initiate SSL context with supported keystore types JKS, JCEKS and PKCS12.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void orgIdClientInit_invalidKeystoreFile_expectKeystoreError() {
        try {
            OrganisationIdClientApi organsiationIdClient = OrganisationIdClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.INVALID_KEYSTORE_FILE), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                    .build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Invalid error", "Failed to initiate SSL context with supported keystore types JKS, JCEKS and PKCS12.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void orgIdClientInit_invalidPoolingTime_expectError() {
        try {
            OrganisationIdClientApi organsiationIdClient = OrganisationIdClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                    .setPollingTimeout(0)
                    .build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Invalid error", "Polling timeout must be between 1 and 30 seconds.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void orgIdClientInit_invalidNullParameter_expectInternalError() {
        try {
            OrganisationIdClientApi organsiationIdClient = OrganisationIdClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, null), FrejaEnvironment.TEST)
                    .build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("KeyStore Path, keyStore password or server certificate path cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void orgIdClientInit_invalidEmptyParameter_expectInternalError() {
        try {
            OrganisationIdClientApi organsiationIdClient = OrganisationIdClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, " "), FrejaEnvironment.TEST)
                    .build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("KeyStore Path, keyStore password or server certificate path cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void orgIdClientInit_success() throws FrejaEidClientInternalException {
        OrganisationIdClientApi organsiationIdClientJKS = OrganisationIdClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                .build();

        OrganisationIdClientApi organsiationIdClientJCEKS = OrganisationIdClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH_JCEKS), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                .build();

        OrganisationIdClientApi organsiationIdClientPKCS12 = OrganisationIdClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH_PKCS12), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                .build();

        OrganisationIdClientApi organsiationIdClientJKSNoServCert = OrganisationIdClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD), FrejaEnvironment.TEST)
                .build();

        OrganisationIdClientApi organsiationIdClientJCEKSNoServCert = OrganisationIdClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH_JCEKS), TestUtil.KEYSTORE_PASSWORD), FrejaEnvironment.TEST)
                .build();
    }
}
