package com.verisec.frejaeid.client.client.builder;

import com.verisec.frejaeid.client.beans.general.SslSettings;
import com.verisec.frejaeid.client.client.api.SignClientApi;
import com.verisec.frejaeid.client.client.impl.SignClient;
import com.verisec.frejaeid.client.client.util.TestUtil;
import com.verisec.frejaeid.client.enums.FrejaEnvironment;
import com.verisec.frejaeid.client.enums.TransactionContext;
import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;
import org.junit.Assert;
import org.junit.Test;

public class SignClientInitialisationTest {

    @Test
    public void signClientInit_invalidKeystorePath_expectKeystoreError() {
        try {
            SignClient.create(SslSettings.create("x", TestUtil.KEYSTORE_PASSWORD, TestUtil.CERTIFICATE_PATH),
                              FrejaEnvironment.TEST).build();
            Assert.fail();
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Failed to initiate SSL context with supported keystore types JKS, JCEKS and PKCS12.",
                                ex.getLocalizedMessage());
        }
    }

    @Test
    public void signClientInit_invalidCertificatePath_expectKeystoreError() {
        try {
            SignClient.create(SslSettings.create(TestUtil.KEYSTORE_PATH, TestUtil.KEYSTORE_PASSWORD, "x"),
                              FrejaEnvironment.TEST).build();
            Assert.fail();
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Failed to initiate SSL context with supported keystore types JKS, JCEKS and PKCS12.",
                                ex.getLocalizedMessage());
        }
    }

    @Test
    public void signClientInit_invalidKeystorePassword_expectKeystoreError() {
        try {
            SignClient.create(SslSettings.create(TestUtil.KEYSTORE_PATH, "111111111", TestUtil.CERTIFICATE_PATH),
                              FrejaEnvironment.TEST).build();
            Assert.fail();
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Failed to initiate SSL context with supported keystore types JKS, JCEKS and PKCS12.",
                                ex.getLocalizedMessage());
        }
    }

    @Test
    public void signClientInit_invalidKeystoreFile_expectKeystoreError() {
        try {
            SignClient.create(SslSettings.create(TestUtil.INVALID_KEYSTORE_FILE, TestUtil.KEYSTORE_PASSWORD,
                                                 TestUtil.CERTIFICATE_PATH), FrejaEnvironment.TEST).build();
            Assert.fail();
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Failed to initiate SSL context with supported keystore types JKS, JCEKS and PKCS12.",
                                ex.getLocalizedMessage());
        }
    }

    @Test
    public void singClientInit_invalidPollingTime_expectError() {
        try {
            SignClient.create(SslSettings.create(TestUtil.KEYSTORE_PATH, TestUtil.KEYSTORE_PASSWORD,
                                                 TestUtil.CERTIFICATE_PATH), FrejaEnvironment.TEST)
                    .setPollingTimeout(500)
                    .build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("Polling timeout must be between 1 and 60 seconds.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void singClientInit_invalidNullParameter_expectInternalError() {
        try {
            SignClient.create(SslSettings.create(TestUtil.KEYSTORE_PATH, null, TestUtil.CERTIFICATE_PATH),
                              FrejaEnvironment.TEST)
                    .setPollingTimeout(0)
                    .build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("KeyStore Path, keyStore password or server certificate path cannot be null or empty.",
                                ex.getLocalizedMessage());
        }
    }

    @Test
    public void singClientInit_invalidEmptyParameter_expectInternalError() {
        try {
            SignClient.create(SslSettings.create(TestUtil.KEYSTORE_PATH, " ", TestUtil.CERTIFICATE_PATH),
                              FrejaEnvironment.TEST)
                    .setPollingTimeout(0)
                    .build();
            Assert.fail("Test should throw exception!");
        } catch (FrejaEidClientInternalException ex) {
            Assert.assertEquals("KeyStore Path, keyStore password or server certificate path cannot be null or empty.",
                                ex.getLocalizedMessage());
        }
    }

    @Test
    public void signClientInit_success() throws FrejaEidClientInternalException {
        SignClientApi signClientJKS = SignClient
                .create(SslSettings.create(TestUtil.KEYSTORE_PATH, TestUtil.KEYSTORE_PASSWORD,
                                           TestUtil.CERTIFICATE_PATH), FrejaEnvironment.TEST)
                .setTransactionContext(TransactionContext.PERSONAL).build();

        SignClientApi signClientJCEKS = SignClient
                .create(SslSettings.create(TestUtil.KEYSTORE_PATH_JCEKS, TestUtil.KEYSTORE_PASSWORD,
                                           TestUtil.CERTIFICATE_PATH), FrejaEnvironment.TEST)
                .setTransactionContext(TransactionContext.PERSONAL).build();

        SignClientApi signClientPKCS12 = SignClient
                .create(SslSettings.create(TestUtil.KEYSTORE_PATH_PKCS12, TestUtil.KEYSTORE_PASSWORD,
                                           TestUtil.CERTIFICATE_PATH), FrejaEnvironment.TEST)
                .setTransactionContext(TransactionContext.PERSONAL).build();

        SignClientApi signClientJKSNoServCert = SignClient
                .create(SslSettings.create(TestUtil.KEYSTORE_PATH, TestUtil.KEYSTORE_PASSWORD), FrejaEnvironment.TEST)
                .setTransactionContext(TransactionContext.PERSONAL).build();

        SignClientApi signClientJCEKSNoServCert = SignClient
                .create(SslSettings.create(TestUtil.KEYSTORE_PATH_JCEKS, TestUtil.KEYSTORE_PASSWORD),
                        FrejaEnvironment.TEST)
                .setTransactionContext(TransactionContext.PERSONAL).build();
    }

}
