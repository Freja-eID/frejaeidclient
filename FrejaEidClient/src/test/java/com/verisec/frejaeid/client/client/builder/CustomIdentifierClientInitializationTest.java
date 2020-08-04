package com.verisec.frejaeid.client.client.builder;

import com.verisec.frejaeid.client.beans.general.SslSettings;
import com.verisec.frejaeid.client.client.impl.CustomIdentifierClient;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import org.junit.Assert;
import org.junit.Test;
import com.verisec.frejaeid.client.client.api.CustomIdentifierClientApi;

public class CustomIdentifierClientInitializationTest {

    @Test
    public void customIdentifierClientInit_invalidKeystorePath_expectKeystoreError() {
        try {
            CustomIdentifierClientApi customIdentifierClient = CustomIdentifierClient.create(SslSettings.create("x", TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                    .build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Failed to initiate SSL context with supported keystore types JKS, JCEKS and PKCS12.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void customIdentifierClientInit_invalidCertificatePath_expectKeystoreError() {
        try {
            CustomIdentifierClientApi customIdentifierClient = CustomIdentifierClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, "x"), FrejaEnvironment.TEST)
                    .build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Failed to initiate SSL context with supported keystore types JKS, JCEKS and PKCS12.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void customIdentifierClientInit_invalidKeystorePassword_expectKeystoreError() {
        try {
            CustomIdentifierClientApi customIdentifierClient = CustomIdentifierClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), "111111111", TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                    .build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Failed to initiate SSL context with supported keystore types JKS, JCEKS and PKCS12.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void customIdentifierClientInit_invalidKeystoreFile_expectKeystoreError() {
        try {
            CustomIdentifierClientApi customIdentifierClient = CustomIdentifierClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.INVALID_KEYSTORE_FILE), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                    .build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Failed to initiate SSL context with supported keystore types JKS, JCEKS and PKCS12.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void customIdentifierClientInit_invalidPoolingTime_expectError() {
        try {
            CustomIdentifierClientApi customIdentifierClient = CustomIdentifierClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                    .setPollingTimeout(500)
                    .build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Polling timeout must be between 1 and 60 seconds.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void customIdentifierClientInit_invalidNullParameter_expectInternalError() {
        try {
            CustomIdentifierClientApi customIdentifierClient = CustomIdentifierClient.create(SslSettings.create(null, TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                    .build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("KeyStore Path, keyStore password or server certificate path cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void customIdentifierClientInit_invalidEmptyParameter_expectInternalError() {
        try {
            CustomIdentifierClientApi customIdentifierClient = CustomIdentifierClient.create(SslSettings.create(" ", TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                    .build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("KeyStore Path, keyStore password or server certificate path cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void customIdentifierClientInit_success() throws FrejaEidClientInternalException {
        CustomIdentifierClientApi customIdentifierClientJKS = CustomIdentifierClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                .build();

        CustomIdentifierClientApi customIdentifierClientJCEKS = CustomIdentifierClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH_JCEKS), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                .build();

        CustomIdentifierClientApi customIdentifierClientPKCS12 = CustomIdentifierClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH_PKCS12), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                .build();

        CustomIdentifierClientApi customIdentifierClientJKSNoServCert = CustomIdentifierClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD), FrejaEnvironment.TEST)
                .build();

        CustomIdentifierClientApi customIdentifierClientJCEKSNoServCert = CustomIdentifierClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH_JCEKS), TestUtil.KEYSTORE_PASSWORD), FrejaEnvironment.TEST)
                .build();
    }

}
