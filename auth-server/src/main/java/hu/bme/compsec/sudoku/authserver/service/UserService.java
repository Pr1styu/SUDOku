package hu.bme.compsec.sudoku.authserver.service;

import hu.bme.compsec.sudoku.authserver.common.UserRole;
import hu.bme.compsec.sudoku.authserver.data.User;
import hu.bme.compsec.sudoku.authserver.data.UserRepository;
import hu.bme.compsec.sudoku.authserver.presentation.dto.UserDTO;
import hu.bme.compsec.sudoku.authserver.presentation.mapping.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        log.info("Checking user with username {}.", userName);
        var userEntity = userRepository.findByUsername(userName)
                .orElseThrow(() -> {
                    log.info("There is no user with username {}.", userName);
                    return new UsernameNotFoundException("There is no user with username: " + userName);
                });
        return userMapper.toSecurityUser(userEntity);
    }

    public boolean createUser(UserDTO dto) {
        // TODO: Checks
        try {
            var savedUser = userRepository.save(userMapper.toEntity(dto));
            log.debug("Successfully saved user: {}", savedUser);
            return true;
        } catch (Exception e) {
            log.error("Error while registering client with username {} due to: {}", dto.getUsername(), e.getMessage());
        }
        return false;
    }


    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void createDefaultUsers() {
        User user = User.builder()
                .username("userke")
                .password(passwordEncoder.encode("password"))
                .email("userke@sudokucaff.com")
                .roles(List.of(UserRole.USER))
                .build();

        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .email("admin@sudokucaff.com")
                .roles(List.of(UserRole.USER, UserRole.ADMIN))
                .build();

        userRepository.save(user);
        log.info("Saving default user {}.", user);
        log.info("Saving default user {}.", admin);
        userRepository.save(admin);
    }


}
