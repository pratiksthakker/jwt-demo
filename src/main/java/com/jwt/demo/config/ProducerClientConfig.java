package com.jwt.demo.config;

import org.apache.http.util.Args;

/**
 * Class representing configurations for ProducerClient.
 */
public class ProducerClientConfig {
  private String demoPublicKeyFile;

  /** Default constructor */
  public ProducerClientConfig() {
  }

  /** All args constructor */
  public ProducerClientConfig(final String demoPublicKeyFile) {
    Args.notBlank(demoPublicKeyFile, "Public Key file");
    this.demoPublicKeyFile = demoPublicKeyFile;
  }

  /** Getter method for public key file location */
  public String getDemoPublicKeyFile() {
    return demoPublicKeyFile;
  }

  /**
   * Setter method for public key file location.
   *
   * @param demoPublicKeyFile public key file location.
   * @throws IllegalArgumentException if public key file location is blank.
   */
  public void setDemoPublicKeyFile(final String demoPublicKeyFile) {
    Args.notBlank(demoPublicKeyFile, "Public Key file");
    this.demoPublicKeyFile = demoPublicKeyFile;
  }
}
