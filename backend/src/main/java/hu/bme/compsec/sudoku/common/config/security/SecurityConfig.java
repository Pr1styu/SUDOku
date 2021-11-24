package hu.bme.compsec.sudoku.common.config.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin() // For h2 GUI only - should remove this in PROD
                .and()
                //.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                .csrf().disable()
                .cors()
                .and()
                .authorizeRequests()
                .mvcMatchers("/swagger-ui/").hasRole(UserRole.ADMIN.name())
                .mvcMatchers("/h2-console/**").hasRole(UserRole.ADMIN.name())
                //.mvcMatchers(HttpMethod.GET, "/caff/", "/caff/{id}").permitAll()
                //.mvcMatchers(HttpMethod.POST, "/caff/*").hasRole(UserRole.ADMIN.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
                //.formLogin()
                //.rememberMe();
    }


    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
