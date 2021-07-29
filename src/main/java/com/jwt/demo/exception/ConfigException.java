package com.jwt.demo.exception;

/**
 * Exception implementation for invalid configurations.
 */
public class ConfigException extends RuntimeException {
  /**
   * Exception with a message.
   *
   * @param message Message for the exception.
   */
  public ConfigException(final String message) {
    super(message);
  }

  /**
   * Exception with a caused by clause for wrapped exception.
   *
   * @param message Message for the exception.
   * @param cause   Wrapped exception.
   */
  public ConfigException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
