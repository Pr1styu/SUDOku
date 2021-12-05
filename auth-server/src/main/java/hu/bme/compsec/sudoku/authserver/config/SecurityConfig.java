package hu.bme.compsec.sudoku.authserver.config;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import hu.bme.compsec.sudoku.common.security.UserRole;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.Set;
import java.util.stream.Collectors;

import static hu.bme.compsec.sudoku.common.security.SecurityUtils.AUTHORITIES_CLAIM;
import static hu.bme.compsec.sudoku.common.security.SecurityUtils.USERID_CLAIM;
import static org.springframework.security.config.Customizer.withDefaults;


@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.cors().and()
				.authorizeRequests(authorizeRequests -> authorizeRequests
						.requestMatchers(PathRequest.toH2Console()).hasRole(UserRole.ADMIN.name())
						.mvcMatchers("/register").permitAll()
						.anyRequest().authenticated()
				)
				.formLogin(withDefaults())
				.csrf(csrfConfig -> csrfConfig
						.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
						.ignoringRequestMatchers(PathRequest.toH2Console())
				)
				.headers().frameOptions().sameOrigin() // For h2 GUI only
				.and()
				.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

		return http.build();
	}

	@Bean
	JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}


	@Bean
	OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
		return context -> {
			if (context.getTokenType().getValue().equals(OAuth2TokenType.ACCESS_TOKEN.getValue())) {
				Authentication principal = context.getPrincipal();
				Set<String> authorities = principal.getAuthorities().stream()
						.map(GrantedAuthority::getAuthority)
						.collect(Collectors.toSet());

				context.getClaims().claim(AUTHORITIES_CLAIM, authorities);
				var securityUser = (SecurityUser) principal.getPrincipal();
				context.getClaims().claim(USERID_CLAIM, securityUser.getId());
			}
		};
	}

}
