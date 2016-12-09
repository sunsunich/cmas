package org.cmas.util.http.ssl;

import javax.net.ssl.X509KeyManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class AliasKeyManager implements X509KeyManager {

    private KeyStore keyStore;
    private String alias;
    private char[] password;

    public AliasKeyManager(String keyStoreFile, char[] password, String alias)
            throws IOException, GeneralSecurityException {
        this.alias = alias;
        this.password = password;
        InputStream stream = new FileInputStream(keyStoreFile);
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(stream, password);
        } finally {
            stream.close();
        }
    }

    public AliasKeyManager(InputStream stream, char[] password, String alias)
            throws IOException, GeneralSecurityException {
        this.alias = alias;
        this.password = password;
        keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(stream, password);
    }

    @Override
    public PrivateKey getPrivateKey(String alias) {
        try {
            return (PrivateKey) keyStore.getKey(alias, password);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public X509Certificate[] getCertificateChain(String alias) {
        try {
            java.security.cert.Certificate[] certs = keyStore.getCertificateChain(alias);
            if (certs == null || certs.length == 0) {
                return null;
            }
            X509Certificate[] x509 = new X509Certificate[certs.length];
            for (int i = 0; i < certs.length; i++) {
                x509[i] = (X509Certificate) certs[i];
            }
            return x509;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String chooseServerAlias(String keyType, Principal[] issuers,
                                    Socket socket) {
        return alias;
    }

    @Override
    public String[] getClientAliases(String parm1, Principal[] parm2) {
        throw new UnsupportedOperationException("Method getClientAliases() not yet implemented.");
    }

    @Override
    public String chooseClientAlias(String keyTypes[], Principal[] issuers, Socket socket) {
        return alias;
    }

    @Override
    public String[] getServerAliases(String parm1, Principal[] parm2) {
        return new String[]{alias};
    }

    public String chooseServerAlias(String parm1, Principal[] parm2) {
        return alias;
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }
}