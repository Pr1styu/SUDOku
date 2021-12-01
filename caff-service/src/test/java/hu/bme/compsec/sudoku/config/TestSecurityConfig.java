package hu.bme.compsec.sudoku.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;
import java.util.Map;

import static hu.bme.compsec.sudoku.common.config.security.SecurityUtils.USERID_CLAIM;

@TestConfiguration
public class TestSecurityConfig {

  static final String TOKEN_NAME = "access_token";
  static final String SUB = "sub";

  @Bean
  public JwtDecoder jwtDecoder() {
    return token -> jwt();
  }

  /**
   * Here we can mock the custom claims.
   * @return
   */
  public Jwt jwt() {

    Map<String, Object> claims = Map.of(
        SUB, "sub", USERID_CLAIM, "1" // TODO Check this!

    );

    return new Jwt(
        TOKEN_NAME,
        Instant.now(),
        Instant.now().plusSeconds(60),
        Map.of("alg", "none"),
        claims
    );
  }

}