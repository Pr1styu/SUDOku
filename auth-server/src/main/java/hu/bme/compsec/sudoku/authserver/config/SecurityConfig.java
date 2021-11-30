package hu.bme.compsec.sudoku.authserver.config;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import hu.bme.compsec.sudoku.authserver.common.UserRole;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.security.config.Customizer.withDefaults;


@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeRequests(authorizeRequests -> authorizeRequests
						.requestMatchers(PathRequest.toH2Console()).hasRole(UserRole.ADMIN.name())
						.mvcMatchers("/register").permitAll()
						.anyRequest().authenticated()
				)
				.formLogin(withDefaults())
				.csrf().ignoringRequestMatchers(PathRequest.toH2Console())
				.and().headers().frameOptions().sameOrigin(); // For h2 GUI only

		return http.build();
	}

	@Bean
	JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	private static final String USERID_CLAIM = "user_id";
	private static final String AUTHORITIES_CLAIM = "authorities";

	@Bean
	OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
		return context -> {
			if (context.getTokenType().getValue().equals(OAuth2TokenType.ACCESS_TOKEN.getValue())) {
				Authentication principal = context.getPrincipal();
				Set<String> authorities = principal.getAuthorities().stream()
						.map(GrantedAuthority::getAuthority)
						.collect(Collectors.toSet());

				context.getClaims().claim(AUTHORITIES_CLAIM, authorities);
				// TODO: Get proper place for this.
				var securityUser = (SecurityUser) principal.getPrincipal();
				context.getClaims().claim(USERID_CLAIM, securityUser.getId());
			}
		};
	}

}
