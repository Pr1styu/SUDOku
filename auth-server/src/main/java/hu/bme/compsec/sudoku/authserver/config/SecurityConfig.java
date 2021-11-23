package hu.bme.compsec.sudoku.authserver.config;

import hu.bme.compsec.sudoku.authserver.common.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.headers().frameOptions().sameOrigin() // For h2 GUI only - should remove this in PROD
				.and()
				.csrf().disable()
				.authorizeRequests()
					.mvcMatchers("/h2-console/**").hasAuthority(UserRole.ADMIN.name())
					.mvcMatchers("/register").permitAll()
				.and()
				.authorizeRequests(authorizeRequests ->
						authorizeRequests.anyRequest().authenticated()
				)
				.formLogin(withDefaults());

		return http.build();
	}

	@Bean
	PasswordEncoder getPasswordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

}
