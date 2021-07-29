package com.jwt.demo.config;

import org.apache.http.util.Args;

public class ConsumerClientConfig {
  private String keyStoreFileLocation;
  private String keyStorePassword;
  private String keyAlias;
  private String consumerKid;
  private long expirationTimeInMinutes;

  /** Getter method for keystore file location. */
  public String getKeyStoreFileLocation() {
    return keyStoreFileLocation;
  }

  /**
   * Setter method for keystore file location.
   *
   * @param keyStoreFileLocation Location of the keystore JKS file.
   * @throws IllegalArgumentException if blank keystore location is provided.
   */
  public void setKeyStoreFileLocation(final String keyStoreFileLocation) {
    Args.notBlank(keyStoreFileLocation, "Key store location");
    this.keyStoreFileLocation = keyStoreFileLocation;
  }

  /** Getter method for keystore password */
  public String getKeyStorePassword() {
    return keyStorePassword;
  }

  /**
   * Setter method for keystore password.
   *
   * @param keyStorePassword the keystore password.
   * @throws IllegalArgumentException if blank password is passed.
   */
  public void setKeyStorePassword(final String keyStorePassword) {
    Args.notBlank(keyStorePassword, "Keystore password");
    this.keyStorePassword = keyStorePassword;
  }

  /** Getter method for key alias */
  public String getKeyAlias() {
    return keyAlias;
  }

  /**
   * Setter method for key alias.
   * @param keyAlias the key alias
   * @throws IllegalArgumentException if blank key alias is passed.
   */
  public void setKeyAlias(final String keyAlias) {
    Args.notBlank(keyAlias, "Key alias");
    this.keyAlias = keyAlias;
  }

  /** Getter method for consumer KID */
  public String getConsumerKid() {
    return consumerKid;
  }

  /**
   * Setter method for consumer KID
   * @param consumerKid the consumer KID
   * @throws IllegalArgumentException If blank consumer KID is passed.
   */
  public void setConsumerKid(final String consumerKid) {
    Args.notBlank(consumerKid, "Consumer Kid");
    this.consumerKid = consumerKid;
  }

  /** Getter method for expiration time in minutes */
  public long getExpirationTimeInMinutes() {
    return expirationTimeInMinutes;
  }

  /**
   * Setter method for expiration time in minutes.
   * @param expirationTimeInMinutes expiration time in minutes.
   * @throws IllegalArgumentException if expiration time is negative.
   */
  public void setExpirationTimeInMinutes(final long expirationTimeInMinutes) {
    this.expirationTimeInMinutes = Args.notNegative(expirationTimeInMinutes, "Expiration time");
  }
}
