package com.jwt.demo.utils;

import com.jwt.demo.config.ProducerClientConfig;
import com.jwt.demo.exception.ConfigException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.Args;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.security.PublicKey;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProducerJwtUtils {

  private static final Logger LOGGER = Logger.getLogger(ProducerJwtUtils.class.getName());
  private static final int TOKEN_BEARER_PART_LENGTH = 7;
  private final ProducerClientConfig config;
  private PublicKey publicKey;

  /** Constructor with Configuration object as parameter */
  public ProducerJwtUtils(final ProducerClientConfig config) {
    this.config = config;
    buildKey();
  }

  /** Builds the {@link PublicKey} object */
  private void buildKey() {
    try {
      this.publicKey = new PemUtils().generatePublicKey(config.getDemoPublicKeyFile());
    } catch (IOException exception) {
      throw new ConfigException("Invalid demo Public key location " + this.config.getDemoPublicKeyFile(), exception);
    }
  }

  /**
   * Validates the provided token as per demo standards.
   *
   * @param token to validate
   * @return Boolean.True if the token is valid, Boolean.False otherwise
   */
  public Boolean validateToken(@NotBlank final String token) {
    Args.notBlank(token, "Token");
    if (!token.startsWith("Bearer ")) {
      return Boolean.FALSE;
    }
    String tokenWithoutBearer = token.substring(TOKEN_BEARER_PART_LENGTH);
    try {
      final Jws<Claims> claimsJws = Jwts.parserBuilder()
            .setSigningKey(this.publicKey)
            .build()
            .parseClaimsJws(tokenWithoutBearer);
      LOGGER.info("Claims Parsed Successfully" + claimsJws.toString());
      final Claims claims = claimsJws.getBody();
      extractClaim(claims, Claims::getSubject);
      LOGGER.info("Claims Body - " + claims.toString());
      return validateHeaders(claimsJws.getHeader()) && validateClaims(claimsJws.getBody());
    } catch (Exception exception) {
      LOGGER.log(Level.SEVERE, exception.getMessage(), exception);
      return false;
    }
  }

  /**
   * Helper method to apply the claim resolver on the claim.
   *
   * @param claims        all claims present in the token
   * @param claimResolver resolver function to get specific claim
   * @param <T>           Type of claim
   * @return Extracted value
   */
  private <T> T extractClaim(final Claims claims, final Function<Claims, T> claimResolver) {
    return claimResolver.apply(claims);
  }

  /**
   * Helper method to check all claims from the token.
   *
   * @param claims All claims present in the token
   * @return true if all claims check out, false otherwise.
   */
  private boolean validateClaims(final Claims claims) {
    String iss = extractClaim(claims, Claims::getIssuer);
    String aud = extractClaim(claims, Claims::getAudience);
    LOGGER.info("JWT Claims Info :" + " Issuer : " + iss + ",Audience : " + aud);
    return "demo".equals(iss)
          && StringUtils.isNotEmpty(aud);
  }

  /**
   * Helper method to validate all the headers in the token.
   *
   * @param header all headers present in the token.
   * @return true of all headers validate, false otherwise.
   */
  @SuppressWarnings("rawtypes")
  private boolean validateHeaders(final JwsHeader header) {
    String typ = header.getType();
    String kid = String.valueOf(header.get("kid"));
    LOGGER.info("JWT Header Info :" + ",type : " + typ + ", Kid:" + kid);
    return "JWT".equals(typ)
          && "demo.1".equals(kid);
  }

}
