package com.verisec.frejaeid.client.beans.general;

import com.verisec.frejaeid.client.exceptions.FrejaEidClientInternalException;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;

/**
 * Encapsulates SSL settings needed for establishing SSL/TLS connection between the client and Freja server.
 * There are multiple ways to initialize the client,
 * either by passing paths of keystore/trustore or by passing a customized SSLContext.
 */
public class SslSettings {

    private String keystorePath;
    private String keystorePass;
    private String truststorePath;
    private String truststorePass;
    private String serverCertificatePath;
    private SSLContext sslContext;

    /**
     * Returns instance of {@link SslSettings} which will create SSL context
     * with given keyStore (supported types are
     * <B>JKS</B>,
     * <B>JCEKS</B>, <B>PKCS12</B>), password and trusted server certificate.
     * <p>
     * In order to establish SSL with server, client should provide path of
     * keyStore and password and path of trusted server certificate.
     *
     * @param keystorePath          absolute path of keyStore file where key pair with
     *                              appropriate SSL certificate is stored.
     * @param keystorePass          password of client keyStore.
     * @param serverCertificatePath absolute path of server certificate.
     * @return sslSettings
     * @throws FrejaEidClientInternalException if any parameter is {@code null}
     *                                         or empty.
     */
    public static SslSettings create(String keystorePath, String keystorePass, String serverCertificatePath)
            throws FrejaEidClientInternalException {
        if (StringUtils.isAnyBlank(keystorePath, keystorePass, serverCertificatePath)) {
            throw new FrejaEidClientInternalException(
                    "KeyStore Path, keyStore password or server certificate path cannot be null or empty.");
        }
        return new SslSettings(keystorePath, keystorePass, null, null, serverCertificatePath);
    }

    /**
     * Returns instance of {@link SslSettings} which will create SSL context
     * with given keyStore (supported types are
     * <B>JKS</B>,
     * <B>JCEKS</B>, <B>PKCS12</B>), password.
     *
     * <B>Use this creator if you have added server certificate to keyStore.</B>
     * <p>
     * In order to establish SSL with server, client should provide path of
     * keyStore and password.
     *
     * @param keystorePath absolute path of keyStore file where key pair with
     *                     appropriate SSL certificate and server certificate are stored.
     * @param keystorePass password of client keyStore.
     * @return sslSettings
     * @throws FrejaEidClientInternalException if any parameter is {@code null}
     *                                         or empty.
     */
    public static SslSettings create(String keystorePath, String keystorePass) throws FrejaEidClientInternalException {
        if (StringUtils.isAnyBlank(keystorePath, keystorePass)) {
            throw new FrejaEidClientInternalException("KeyStore Path or keyStore password cannot be null or empty.");
        }
        return new SslSettings(keystorePath, keystorePass, null, null, null);
    }

    /**
     * Returns instance of {@link SslSettings} which will create SSL context
     * with given keyStore (supported types are
     * <B>JKS</B>,
     * <B>JCEKS</B>, <B>PKCS12</B>), password.
     *
     * <B>Use this creator if you have added server certificate to keyStore.</B>
     * <p>
     * In order to establish SSL with server, client should provide path of
     * keyStore and password.
     *
     * @param keystorePath absolute path of keyStore file where key pair with
     *                     appropriate SSL certificate and server certificate are stored.
     * @param keystorePass password of client keyStore.
     * @return sslSettings
     * @throws FrejaEidClientInternalException if any parameter is {@code null}
     *                                         or empty.
     */
    public static SslSettings create(String keystorePath,
                                     String keystorePass,
                                     String truststorePath,
                                     String truststorePass) throws FrejaEidClientInternalException {
        if (StringUtils.isAnyBlank(keystorePath, keystorePass)) {
            throw new FrejaEidClientInternalException("Keystore path or password cannot be null or empty.");
        }
        if (StringUtils.isAnyBlank(truststorePath, truststorePass)) {
            throw new FrejaEidClientInternalException("Truststore path or password password cannot be null or empty.");
        }
        return new SslSettings(keystorePath, keystorePass, truststorePath, truststorePass, null);
    }

    /**
     * Returns instance of {@link SslSettings} created with given SslContext. In
     * order to establish SSL with server, client should provide SSLContext
     * object from javax.net.ssl package.
     *
     * @param sslContext created using keyStore file where key pair with
     *                   appropriate SSL certificate is stored.
     * @return sslSettings
     */
    public static SslSettings create(SSLContext sslContext) {
        return new SslSettings(sslContext);
    }

    /**
     * Returns instance of {@link SslSettings} which will create SSL context
     * with given keyStore and truststore (supported types for both stores are
     * <B>JKS</B>,
     * <B>JCEKS</B>, <B>PKCS12</B>), password and trusted server certificate.
     * <p>
     * In order to establish SSL with server, client should provide path and password of
     * keystore and path and password of truststore.
     *
     * @param keystorePath          absolute path of keyStore file where key pair with
     *                              appropriate SSL certificate is stored.
     * @param keystorePass          password of client keyStore.
     * @param truststorePath          absolute path of trustStore file where trusted certificates
     *                              are stored.
     * @param truststorePass          password of trustStore.
     * @return sslSettings
     * @throws FrejaEidClientInternalException if any parameter is {@code null}
     *                                         or empty.
     */
    private SslSettings(String keystorePath,
                        String keystorePass,
                        String truststorePath,
                        String truststorePass,
                        String serverCertificatePath) {
        this.keystorePath = keystorePath;
        this.keystorePass = keystorePass;
        this.truststorePath = truststorePath;
        this.truststorePass = truststorePass;
        this.serverCertificatePath = serverCertificatePath;
    }

    private SslSettings(SSLContext sslContext) {
        this.sslContext = sslContext;
    }

    public String getKeystorePath() {
        return keystorePath;
    }

    public String getKeystorePass() {
        return keystorePass;
    }

    public String getTruststorePath() {
        return truststorePath;
    }

    public String getTruststorePass() {
        return truststorePass;
    }

    public SSLContext getSslContext() {
        return sslContext;
    }

    public String getServerCertificatePath() {
        return serverCertificatePath;
    }

}
