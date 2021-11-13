package hu.bme.compsec.sudoku.service;

import hu.bme.compsec.sudoku.common.config.security.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private PasswordEncoder encoder;

    // TODO: Improve user handling
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("userke")
                .password(encoder.encode("password"))
                //.roles(UserRole.USER.name())
                .authorities(UserRole.USER.getGrantedAuthorities())
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode("admin"))
                //.roles(UserRole.USER.name(),UserRole.ADMIN.name())
                .authorities(UserRole.ADMIN.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(
                user, admin
        );
    }

}
