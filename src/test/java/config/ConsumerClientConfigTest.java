package config;

import com.jwt.demo.config.ConsumerClientConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConsumerClientConfigTest {
  private ConsumerClientConfig config;

  @BeforeEach
  void setUp() {
    config = new ConsumerClientConfig();
  }

  @Test
  void setKeyStoreFileLocation() {
    helper(config::setKeyStoreFileLocation,
          config::getKeyStoreFileLocation);
  }

  @Test
  void setKeyStorePassword() {
    helper(config::setKeyStorePassword, config::getKeyStorePassword);
  }

  @Test
  void setKeyAlias() {
    helper(config::setKeyAlias, config::getKeyAlias);
  }

  @Test
  void setConsumerKid() {
    helper(config::setConsumerKid, config::getConsumerKid);
  }

  @Test
  void setExpirationTimeInMinutes() {
    assertThrows(IllegalArgumentException.class, () -> config.setExpirationTimeInMinutes(-1));
    config.setExpirationTimeInMinutes(100L);
    assertEquals(100L, config.getExpirationTimeInMinutes());
  }

  private void helper(Consumer<String> consumer, Supplier<String> supplier) {
    Stream.of(null, "", "  ").forEach(
          x -> assertThrows(IllegalArgumentException.class, () -> consumer.accept(x))
    );
    consumer.accept("valid");
    assertEquals(supplier.get(), "valid");
  }
}
