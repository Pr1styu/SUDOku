package hu.bme.compsec.sudoku.common.config.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    @Profile("dev")
    SecurityFilterChain securityFilterChainDev(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .mvcMatchers("/swagger-ui/**").hasRole(UserRole.ADMIN.name())
                        .requestMatchers(PathRequest.toH2Console()).hasRole(UserRole.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults())
                .csrf().ignoringRequestMatchers(PathRequest.toH2Console())
                .and().headers().frameOptions().sameOrigin(); // For h2 GUI only

        return http.build();
    }

    @Bean
    @Profile("dev")
    UserDetailsService users() {
        var user = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .authorities(UserRole.ADMIN.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    @Profile("prod")
    SecurityFilterChain securityFilterChainJWT(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(PathRequest.toH2Console()).permitAll() // TODO: Turn this off once the db model finalized
                        .mvcMatchers("/swagger-resources/**", "/swagger-ui/**", "/webjars/springfox-swagger-ui/**", "/v2/api-docs**", "/swagger**")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(getJwtAuthenticationConverter());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static final String AUTHORITIES_CLAIM = "authorities";

    private Converter<Jwt, AbstractAuthenticationToken> getJwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName(AUTHORITIES_CLAIM);
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }


}