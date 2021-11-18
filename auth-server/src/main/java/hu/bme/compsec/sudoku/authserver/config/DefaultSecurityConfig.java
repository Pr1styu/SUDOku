package hu.bme.compsec.sudoku.authserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@EnableWebSecurity
public class DefaultSecurityConfig {

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http
				.headers().frameOptions().sameOrigin() // For h2 GUI only - should remove this in PROD
			.and()
				.csrf().disable()
				.authorizeRequests().mvcMatchers("/h2-console/**").hasRole("ADMIN")
			.and()
				.authorizeRequests(authorizeRequests ->
						authorizeRequests.anyRequest().authenticated()
				)
				.formLogin(withDefaults());

		return http.build();
	}

	@Bean
	UserDetailsService users() {
		UserDetails user = User.withDefaultPasswordEncoder()
				.username("user1")
//				.password(getPasswordEncoder().encode("password"))
				.password("password")
				.roles("USER")
				.build();

//		UserDetails admin = User.withDefaultPasswordEncoder()
//				.username("admin1")
//				.password(getPasswordEncoder().encode("admin"))
//				.roles("USER", "ADMIN")
//				.build();


		return new InMemoryUserDetailsManager(user);
	}

	/*@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}*/

}
