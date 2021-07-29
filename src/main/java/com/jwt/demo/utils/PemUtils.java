package com.jwt.demo.utils;

import java.io.FileReader;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

/**
 * Utility class to generate keys from PEM file.
 */
@SuppressWarnings("unused")
public class PemUtils {
    /**
     * Generates a {@code PrivateKey} from given PEM file location.
     *
     * @param privateKeyFile PEM file location.
     * @return {@code PrivateKey}
     * @throws IOException in case the PEM file could not be read.
     */
    public PrivateKey generatePrivateKey(final String privateKeyFile) throws IOException {
        try (PEMParser pemParser = new PEMParser(new FileReader(privateKeyFile))) {
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            Object keyPair = pemParser.readObject();
            return converter.getPrivateKey(((PEMKeyPair) keyPair).getPrivateKeyInfo());
      /*if (keyPair instanceof PrivateKeyInfo) {
        return converter.getPrivateKey((PrivateKeyInfo) keyPair);
      } else {
        return converter.getPrivateKey(((PEMKeyPair) keyPair).getPrivateKeyInfo());
      }*/
        }
    }

    /**
     * Generates a {@code PublicKey} from given PEM file location.
     *
     * @param publicKeyFile PEM file location.
     * @return {@code PublicKey}
     * @throws IOException in case the PEM file could not be read.
     */
    public PublicKey generatePublicKey(final String publicKeyFile) throws IOException {
        try (PEMParser pemParser = new PEMParser(new FileReader(publicKeyFile))) {
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            return converter.getPublicKey((SubjectPublicKeyInfo) pemParser.readObject());
        }
    }
}