package utils;

import com.jwt.demo.config.ConsumerClientConfig;
import com.jwt.demo.exception.ConfigException;
import com.jwt.demo.utils.ConsumerJwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ConsumerJwtUtilsTest {

  private static final String VALID_KEYSTORE = "src/test/resources/keystore.jks";
  private static final String VALID_KEY_ALIAS = "rc1.oracle.com";
  private static final String VALID_KEYSTORE_TRUSTSTORE_PASSWORD = "password123";
  private static final String CONSUMER_KID = "demo.1";

  ConsumerJwtUtils utils;
  ConsumerClientConfig config;

  @BeforeEach
  void setUp() {
    config = new ConsumerClientConfig();
    config.setKeyStoreFileLocation(VALID_KEYSTORE);
    config.setKeyStorePassword(VALID_KEYSTORE_TRUSTSTORE_PASSWORD);
    config.setConsumerKid(CONSUMER_KID);
    config.setKeyAlias(VALID_KEY_ALIAS);
    config.setExpirationTimeInMinutes(10L);
    utils = new ConsumerJwtUtils(config);
  }

  @Test
  void getJwt() {
    Stream.of(null, "", "  ").forEach(
          x -> assertThrows(IllegalArgumentException.class, () -> utils.getJwt(x))
    );
    String jwt = utils.getJwt("/the/endpoint");
    assertTrue(jwt.startsWith("Bearer "));
    assertEquals(3, jwt.substring(7).split("\\.").length);
  }

  @Test
  void testBuildKeyWithInvalidKeyLocation() {
    config.setKeyStoreFileLocation("Invalid");
    assertThrows(ConfigException.class, () -> new ConsumerJwtUtils(config));
  }

  @Test
  void testBuildKeyWithInvalidKeyAlias() {
    config.setKeyAlias("Invalid");
    assertThrows(ConfigException.class, () -> new ConsumerJwtUtils(config));
  }
}
