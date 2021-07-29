package config;

import com.jwt.demo.config.ProducerClientConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProducerClientConfigTest {

  @Test
  @DisplayName("Constructor validation test")
  public void constructorValidationTest() {
    Assertions.assertThrows(IllegalArgumentException.class,
          () -> new ProducerClientConfig(null));
    Assertions.assertThrows(IllegalArgumentException.class,
          () -> new ProducerClientConfig(""));
    Assertions.assertThrows(IllegalArgumentException.class,
          () -> new ProducerClientConfig("  "));
  }

  @Test
  void getPepPublicKeyFile() {
    ProducerClientConfig config = new ProducerClientConfig("Some key location");
    Assertions.assertEquals(config.getDemoPublicKeyFile(), "Some key location");
  }

  @Test
  void setPepPublicKeyFile() {
    ProducerClientConfig config = new ProducerClientConfig();
    config.setDemoPublicKeyFile("Some key location");
    Assertions.assertEquals(config.getDemoPublicKeyFile(), "Some key location");
  }
}
