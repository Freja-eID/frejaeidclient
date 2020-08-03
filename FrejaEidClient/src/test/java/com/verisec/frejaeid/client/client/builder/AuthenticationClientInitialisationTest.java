package com.verisec.frejaeid.client.client.builder;

import com.verisec.frejaeid.client.beans.general.SslSettings;
import com.verisec.frejaeid.client.client.api.AuthenticationClientApi;
import com.verisec.frejaeid.client.client.impl.AuthenticationClient;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.enums.TransactionContext;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import com.verisec.frejaeid.client.exceptions.FrejaEidException;
import org.junit.Assert;
import org.junit.Test;

public class AuthenticationClientInitialisationTest {

    @Test
    public void authClientInit_invalidKeystorePath_expectKeystoreError() {
        try {
            AuthenticationClientApi authenticationClient = AuthenticationClient.create(SslSettings.create("x", TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                    .build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Failed to initiate SSL context with supported keystore types JKS, JCEKS and PKCS12.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void authClientInit_invalidCertificatePath_expectKeystoreError() {
        try {
            AuthenticationClientApi authenticationClient = AuthenticationClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, "x"), FrejaEnvironment.TEST)
                    .build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Failed to initiate SSL context with supported keystore types JKS, JCEKS and PKCS12.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void authClientInit_invalidKeystorePassword_expectKeystoreError() {
        try {
            AuthenticationClientApi authenticationClient = AuthenticationClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), "111111111", TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                    .build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Failed to initiate SSL context with supported keystore types JKS, JCEKS and PKCS12.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void authClientInit_invalidKeystoreFileAuthentication_expectKeystoreError() {
        try {
            AuthenticationClientApi authenticationClient = AuthenticationClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.INVALID_KEYSTORE_FILE), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                    .build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Failed to initiate SSL context with supported keystore types JKS, JCEKS and PKCS12.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void authClientInit_invalidPoolingTime_expectError() {
        try {
            AuthenticationClientApi authenticationClient = AuthenticationClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                    .setPollingTimeout(500)
                    .build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Polling timeout must be between 1 and 60 seconds.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void authClientInit_invalidNullParameter_expectInternalError() {
        try {
            AuthenticationClientApi authenticationClient = AuthenticationClient.create(SslSettings.create(null, TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                    .build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("KeyStore Path, keyStore password or server certificate path cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void authClientInit_invalidEmptyParameter_expectInternalError() {
        try {
            AuthenticationClientApi authenticationClient = AuthenticationClient.create(SslSettings.create(" ", TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                    .build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("KeyStore Path, keyStore password or server certificate path cannot be null or empty.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void authClientInit_success() throws FrejaEidClientInternalException, FrejaEidException {
        AuthenticationClientApi authenticationClientJKS = AuthenticationClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                .setTransactionContext(TransactionContext.PERSONAL).build();

        AuthenticationClientApi authenticationClientJCEKS = AuthenticationClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH_JCEKS), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                .setTransactionContext(TransactionContext.PERSONAL).build();

        AuthenticationClientApi authenticationClientPKCS12 = AuthenticationClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH_PKCS12), TestUtil.KEYSTORE_PASSWORD, TestUtil.getKeystorePath(TestUtil.CERTIFICATE_PATH)), FrejaEnvironment.TEST)
                .setTransactionContext(TransactionContext.PERSONAL).build();

        AuthenticationClientApi authenticationClientJKSNoServCert = AuthenticationClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH), TestUtil.KEYSTORE_PASSWORD), FrejaEnvironment.TEST)
                .setTransactionContext(TransactionContext.PERSONAL).build();

        AuthenticationClientApi authenticationClientJCEKSNoServCert = AuthenticationClient.create(SslSettings.create(TestUtil.getKeystorePath(TestUtil.KEYSTORE_PATH_JCEKS), TestUtil.KEYSTORE_PASSWORD), FrejaEnvironment.TEST)
                .setTransactionContext(TransactionContext.PERSONAL).build();
    }
}
