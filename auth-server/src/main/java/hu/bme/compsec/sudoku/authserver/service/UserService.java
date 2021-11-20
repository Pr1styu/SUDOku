package hu.bme.compsec.sudoku.authserver.service;

import hu.bme.compsec.sudoku.authserver.common.UserRole;
import hu.bme.compsec.sudoku.authserver.data.User;
import hu.bme.compsec.sudoku.authserver.data.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private PasswordEncoder encoder;

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with username: " + userName));
    }

    @PostConstruct
    public void createDefaultUsers() {
        User user = User.builder()
                .userName("userke")
                .password(encoder.encode("password"))
                .email("userke@sudokucaff.com")
                .roles(List.of(UserRole.USER))
                .build();

        User admin = User.builder()
                .userName("admin")
                .password(encoder.encode("admin"))
                .email("admin@sudokucaff.com")
                .roles(List.of(UserRole.USER, UserRole.ADMIN))
                .enabled(true)
                .build();

        userRepository.save(user);
        userRepository.save(admin);
    }


}
