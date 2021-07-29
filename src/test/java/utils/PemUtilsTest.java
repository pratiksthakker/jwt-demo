package utils;

import com.jwt.demo.utils.PemUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PemUtilsTest {

  @Test
  void generatePrivateKey() throws IOException {
    assertNotNull(new PemUtils().generatePrivateKey("src/test/resources/private_key.pem"));
  }

  @Test
  void generatePublicKey() throws IOException {
    assertNotNull(new PemUtils().generatePublicKey("src/test/resources/public_key.pem"));
  }
}
