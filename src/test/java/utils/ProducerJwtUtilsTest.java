package utils;

import com.jwt.demo.config.ConsumerClientConfig;
import com.jwt.demo.config.ProducerClientConfig;
import com.jwt.demo.utils.ConsumerJwtUtils;
import com.jwt.demo.utils.PemUtils;
import com.jwt.demo.utils.ProducerJwtUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ProducerJwtUtilsTest {

  private static final String VALID_KEYSTORE = "src/test/resources/keystore.jks";
  private static final String VALID_KEY_ALIAS = "rc1.oracle.com";
  private static final String VALID_KEYSTORE_TRUSTSTORE_PASSWORD = "password123";
  private static final String CONSUMER_KID = "demo.1";
  ProducerJwtUtils producerJwtUtils;
  ConsumerJwtUtils consumerJwtUtils;
  private ConsumerClientConfig consumerConfig;

  @BeforeEach
  void setUp() {
    ProducerClientConfig config = new ProducerClientConfig("src/test/resources/dummy_public.pem");
    producerJwtUtils = new ProducerJwtUtils(config);

    consumerConfig = new ConsumerClientConfig();
    consumerConfig.setKeyStoreFileLocation(VALID_KEYSTORE);
    consumerConfig.setKeyStorePassword(VALID_KEYSTORE_TRUSTSTORE_PASSWORD);
    consumerConfig.setConsumerKid(CONSUMER_KID);
    consumerConfig.setKeyAlias(VALID_KEY_ALIAS);
    consumerConfig.setExpirationTimeInMinutes(10L);
    consumerJwtUtils = new ConsumerJwtUtils(consumerConfig);
  }

  @Test
  @DisplayName("Validate valid token")
  void validateToken() {
    String jwt = consumerJwtUtils.getJwt("/the/endpoint");
    assertTrue(producerJwtUtils.validateToken(jwt));
  }

  @Test
  @DisplayName("Validate invalid token")
  void validateInvalidToken() {
    Stream.of(null, "", "  ").forEach(
          x -> assertThrows(IllegalArgumentException.class, () -> producerJwtUtils.validateToken(x))
    );
    assertFalse(producerJwtUtils.validateToken("DoesNotStartWithBearer"));
  }

  @Test
  @DisplayName("Validate token wrong signature")
  public void invalidSignature() {
    String jwt = consumerJwtUtils.getJwt("/the/endpoint");
    ProducerClientConfig producerConfig = new ProducerClientConfig("src/test/resources/dummy2.pem");
    producerJwtUtils = new ProducerJwtUtils(producerConfig);
    assertFalse(producerJwtUtils.validateToken(jwt));
  }

  @Test
  @DisplayName("Validate token with different claims")
  public void validateTokenWithDifferentClaims() throws IOException {
    Map<String, String> claimsAndHeaders = Stream.of(new String[][]{
          {"typ", "JWT"},
          {"kid", "demo.1"},
          {"iss", "demo"},
          {"aud", "/glmp/service/v1"},
          {"exp", String.valueOf(System.currentTimeMillis() + (10 * 60000))}
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
    assertTrue(producerJwtUtils.validateToken(buildToken(claimsAndHeaders)));
    claimsAndHeaders.put("typ", "NotJwt");
    assertFalse(producerJwtUtils.validateToken(buildToken(claimsAndHeaders)));
    claimsAndHeaders.put("typ", "JWT");
    claimsAndHeaders.put("kid", "goals.1");
    assertFalse(producerJwtUtils.validateToken(buildToken(claimsAndHeaders)));
    claimsAndHeaders.put("kid", "demo.1");
    claimsAndHeaders.put("iss", "notdemo");
    assertFalse(producerJwtUtils.validateToken(buildToken(claimsAndHeaders)));
    claimsAndHeaders.put("iss", "demo");
    claimsAndHeaders.remove("aud");
    assertFalse(producerJwtUtils.validateToken(buildToken(claimsAndHeaders)));
    claimsAndHeaders.put("aud", "/glmp/service/v1");
    claimsAndHeaders.put("exp", String.valueOf(System.currentTimeMillis() - (10 * 60000)));
    assertFalse(producerJwtUtils.validateToken(buildToken(claimsAndHeaders)));
  }

  private String buildToken(Map<String, String> claimsAndHeaders) throws IOException {
    Key privateKey = new PemUtils().generatePrivateKey("src/test/resources/dummy_pvt.pem");
    return "Bearer " + Jwts.builder()
          .signWith(privateKey, SignatureAlgorithm.RS256)
          .setHeaderParam("typ", claimsAndHeaders.get("typ"))
          .setHeaderParam("kid", claimsAndHeaders.get("kid"))
          .setIssuer(claimsAndHeaders.get("iss"))
          .setAudience(claimsAndHeaders.get("aud"))
          .setExpiration(new Date(Long.parseLong(claimsAndHeaders.get("exp"))))
          .setHeaderParam("alg", claimsAndHeaders.get("alg"))
          .setId(UUID.randomUUID().toString())
          .compact();
  }

}
