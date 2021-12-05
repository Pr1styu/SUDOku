package config;

import hu.bme.compsec.sudoku.common.security.UserRole;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import java.time.Instant;
import java.util.Map;
import java.util.Random;

import static hu.bme.compsec.sudoku.common.security.SecurityUtils.USERID_CLAIM;

@TestConfiguration
public class TestSecurityConfig {

  static final String TOKEN_NAME = "access_token";
  static final String SUB = "sub";

  static Long mockUserId = 1L;

  private static final Random random = new Random();

  public static long getRandomId() {
    return random.nextInt(100);
  }

  public static void mockAuthenticatedUserId(Long id) {
    TestSecurityConfig.mockUserId = id;
  }

  public static void mockAuthWithUserRoleAndId(UserRole userRole) {
    JwtAuthenticationToken jwt = new JwtAuthenticationToken(
            jwt(),
            userRole.getGrantedAuthorities()
    );
    jwt.setAuthenticated(true);

    var ctx = new SecurityContextImpl();
    ctx.setAuthentication(jwt);
    SecurityContextHolder.setContext(ctx);
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    return token -> jwt();
  }

  /**
   * Here we can mock the JWT with custom claims.
   */
  public static Jwt jwt() {
    Map<String, Object> claims = Map.of(
            SUB, "sub",
            USERID_CLAIM, mockUserId
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