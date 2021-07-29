package com.jwt.demo.utils;

import com.jwt.demo.config.ConsumerClientConfig;
import com.jwt.demo.exception.ConfigException;
import io.jsonwebtoken.Jwts;

import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.util.Date;
import java.util.UUID;
import javax.net.ssl.KeyManagerFactory;

import org.apache.http.util.Args;

public class ConsumerJwtUtils {

    private static final int MINUTES_MULTIPLIER = 60000;
    private final ConsumerClientConfig config;
    private Key privateKey;

    /** Constructor with Configuration */
    public ConsumerJwtUtils(final ConsumerClientConfig config) {
        this.config = config;
        buildKey();
    }

    /** Initializes the Private key object */
    private void buildKey() {
        Args.notNull(config, "Configuration");
        File keyStoreFile = new File(config.getKeyStoreFileLocation());
        try (FileInputStream keyStoreInputStream = new FileInputStream(keyStoreFile)) {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(keyStoreInputStream, config.getKeyStorePassword().toCharArray());
            KeyManagerFactory keyManagerFactory =
                    KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, config.getKeyStorePassword().toCharArray());
            Key key = keyStore.getKey(config.getKeyAlias(), config.getKeyStorePassword().toCharArray());
            if (key == null) {
                throw new ConfigException(
                        "Could not create a key. May be Alias is incorrect or invalid keystore is provided.");
            }
            this.privateKey = key;
        } catch (Exception exception) {
            throw new ConfigException(exception.getMessage(), exception);
        }
    }

    /**
     * Generates the JWT token for the given endpoints.
     * @param endpoint Endpoint is added to {@code aud} token.
     * @return the JWT token.
     * @throws IllegalArgumentException if the endpoint is blank.
     */
    public String getJwt(final String endpoint) {
        Args.notBlank(endpoint, "endpoint");
        String shortCode = this.config.getConsumerKid().split("\\.")[0];

        return "Bearer " + Jwts.builder()
                .signWith(privateKey)
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("kid", config.getConsumerKid())
                .setIssuer(shortCode)
                .setAudience(endpoint)
                .setExpiration(new Date(System.currentTimeMillis()
                        + (config.getExpirationTimeInMinutes()
                        * MINUTES_MULTIPLIER)))
                .setId(UUID.randomUUID().toString())
                .compact();
    }


}
