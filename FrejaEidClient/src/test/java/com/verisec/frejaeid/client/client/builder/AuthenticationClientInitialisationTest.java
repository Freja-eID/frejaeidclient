package com.verisec.frejaeid.client.client.builder;

import com.verisec.frejaeid.client.beans.general.SslSettings;
import com.verisec.frejaeid.client.client.api.AuthenticationClientApi;
import com.verisec.frejaeid.client.client.impl.AuthenticationClient;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.enums.KeyStoreType;
import com.verisec.frejaeid.client.enums.TransactionContext;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AuthenticationClientInitialisationTest {

    @Test
    public void authClientInit_invalidKeystorePath_expectKeystoreError() {
        try {
            AuthenticationClient.create(SslSettings.create("x", TestUtil.KEYSTORE_PASSWORD,
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
    public void authClientInit_invalidCertificatePath_expectKeystoreError() {
        try {
            AuthenticationClient.create(SslSettings.create(TestUtil.KEYSTORE_PATH, TestUtil.KEYSTORE_PASSWORD, "x"),
                                        FrejaEnvironment.TEST).build();
            fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            assertEquals("Failed to create trust store with certificate at path x.", ex.getMessage());
        }
    }

    @Test
    public void authClientInit_invalidTruststorePath_expectKeystoreError() {
        try {
            AuthenticationClient.create(SslSettings.create(TestUtil.KEYSTORE_PATH, TestUtil.KEYSTORE_PASSWORD,
                                                           "x", TestUtil.TRUSTSTORE_PASSWORD),
                                        FrejaEnvironment.TEST).build();
            fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            assertEquals(
                    String.format("Failed to load keystore at path %s with supported keystore types %s.",
                                  "x", KeyStoreType.getAllKeyStoreTypes()),
                    ex.getMessage());
        }
    }

    @Test
    public void authClientInit_invalidKeystorePassword_expectKeystoreError() {
        try {
            AuthenticationClient.create(SslSettings.create(TestUtil.KEYSTORE_PATH, "111111111",
                                                           TestUtil.CERTIFICATE_PATH),
                                        FrejaEnvironment.TEST).build();
            fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            assertEquals(
                    String.format("Failed to load keystore at path %s with supported keystore types %s.",
                                  TestUtil.KEYSTORE_PATH, KeyStoreType.getAllKeyStoreTypes()),
                    ex.getMessage());
        }
    }

    @Test
    public void authClientInit_invalidKeystoreFile_expectKeystoreError() {
        try {
            AuthenticationClient.create(SslSettings.create(TestUtil.INVALID_KEYSTORE_FILE, TestUtil.KEYSTORE_PASSWORD,
                                                           TestUtil.CERTIFICATE_PATH), FrejaEnvironment.TEST).build();
            fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            assertEquals(String.format("Failed to load keystore at path %s with supported keystore types %s.",
                                       TestUtil.INVALID_KEYSTORE_FILE, KeyStoreType.getAllKeyStoreTypes()),
                         ex.getMessage());
        }
    }

    @Test
    public void authClientInit_invalidTruststoreFile_expectKeystoreError() {
        try {
            AuthenticationClient.create(SslSettings.create(
                                                TestUtil.KEYSTORE_PATH, TestUtil.KEYSTORE_PASSWORD,
                                                TestUtil.INVALID_TRUSTSTORE_FILE, TestUtil.TRUSTSTORE_PASSWORD),
                                        FrejaEnvironment.TEST).build();
            fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            assertEquals(String.format("Failed to load keystore at path %s with supported keystore types %s.",
                                       TestUtil.INVALID_TRUSTSTORE_FILE, KeyStoreType.getAllKeyStoreTypes()),
                         ex.getMessage());
        }
    }

    @Test
    public void authClientInit_invalidPollingTime_expectError() {
        try {
            AuthenticationClient.create(SslSettings.create(TestUtil.KEYSTORE_PATH, TestUtil.KEYSTORE_PASSWORD,
                                                           TestUtil.CERTIFICATE_PATH), FrejaEnvironment.TEST)
                    .setPollingTimeout(500)
                    .build();
            fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            assertEquals("Polling timeout must be between 1 and 60 seconds.", ex.getMessage());
        }
    }

    @Test
    public void authClientInit_invalidNullParameter_expectInternalError() {
        try {
            AuthenticationClient.create(SslSettings.create(null, TestUtil.KEYSTORE_PASSWORD, TestUtil.CERTIFICATE_PATH),
                                        FrejaEnvironment.TEST).build();
            fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            assertEquals("KeyStore Path, keyStore password or server certificate path cannot be null or empty.",
                         ex.getMessage());
        }
    }

    @Test
    public void authClientInit_invalidEmptyParameter_expectInternalError() {
        try {
            AuthenticationClient.create(SslSettings.create(" ", TestUtil.KEYSTORE_PASSWORD,
                                                           TestUtil.CERTIFICATE_PATH), FrejaEnvironment.TEST).build();
            fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            assertEquals("KeyStore Path, keyStore password or server certificate path cannot be null or empty.",
                         ex.getMessage());
        }
    }

    @Test
    public void authClientInit_success() throws FrejaEidClientInternalException {
        AuthenticationClientApi authenticationClientJKS =
                AuthenticationClient.create(SslSettings.create(TestUtil.KEYSTORE_PATH, TestUtil.KEYSTORE_PASSWORD,
                                                               TestUtil.CERTIFICATE_PATH), FrejaEnvironment.TEST)
                        .setTransactionContext(TransactionContext.PERSONAL).build();

        AuthenticationClientApi authenticationClientJCEKS =
                AuthenticationClient.create(SslSettings.create(TestUtil.KEYSTORE_PATH_JCEKS,
                                                               TestUtil.KEYSTORE_PASSWORD, TestUtil.CERTIFICATE_PATH),
                                            FrejaEnvironment.TEST)
                        .setTransactionContext(TransactionContext.PERSONAL).build();

        AuthenticationClientApi authenticationClientPKCS12 =
                AuthenticationClient.create(SslSettings.create(TestUtil.KEYSTORE_PATH_PKCS12,
                                                               TestUtil.KEYSTORE_PASSWORD, TestUtil.CERTIFICATE_PATH),
                                            FrejaEnvironment.TEST)
                        .setTransactionContext(TransactionContext.PERSONAL).build();

        AuthenticationClientApi authenticationClientJKSNoServCert =
                AuthenticationClient.create(SslSettings.create(TestUtil.KEYSTORE_PATH, TestUtil.KEYSTORE_PASSWORD),
                                            FrejaEnvironment.TEST)
                        .setTransactionContext(TransactionContext.PERSONAL).build();

        AuthenticationClientApi authenticationClientJCEKSNoServCert =
                AuthenticationClient.create(SslSettings.create(TestUtil.KEYSTORE_PATH_JCEKS,
                                                               TestUtil.KEYSTORE_PASSWORD), FrejaEnvironment.TEST)
                        .setTransactionContext(TransactionContext.PERSONAL).build();

        AuthenticationClientApi authenticationClientJKSWithTruststore =
                AuthenticationClient.create(SslSettings.create(TestUtil.KEYSTORE_PATH, TestUtil.KEYSTORE_PASSWORD,
                                                               TestUtil.TRUSTSTORE_PATH, TestUtil.TRUSTSTORE_PASSWORD),
                                            FrejaEnvironment.TEST)
                        .setTransactionContext(TransactionContext.PERSONAL).build();

        AuthenticationClientApi authenticationClientJCEKSWithTruststore =
                AuthenticationClient.create(SslSettings.create(
                                                    TestUtil.KEYSTORE_PATH_JCEKS, TestUtil.KEYSTORE_PASSWORD,
                                                    TestUtil.TRUSTSTORE_PATH_JCEKS, TestUtil.TRUSTSTORE_PASSWORD),
                                            FrejaEnvironment.TEST)
                        .setTransactionContext(TransactionContext.PERSONAL).build();

        AuthenticationClientApi authenticationClientPKCS12WithTruststore =
                AuthenticationClient.create(
                                SslSettings.create(TestUtil.KEYSTORE_PATH_PKCS12, TestUtil.KEYSTORE_PASSWORD,
                                                   TestUtil.TRUSTSTORE_PATH_PKCS12, TestUtil.TRUSTSTORE_PASSWORD),
                                FrejaEnvironment.TEST)
                        .setTransactionContext(TransactionContext.PERSONAL).build();
    }
}
