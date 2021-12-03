package hu.bme.compsec.sudoku.authserver.service;

import hu.bme.compsec.sudoku.authserver.common.exception.UserNotFoundException;
import hu.bme.compsec.sudoku.authserver.common.exception.UsernameAlreadyInUseException;
import hu.bme.compsec.sudoku.authserver.data.User;
import hu.bme.compsec.sudoku.authserver.data.UserRepository;
import hu.bme.compsec.sudoku.authserver.presentation.dto.UserProfileDTO;
import hu.bme.compsec.sudoku.authserver.presentation.dto.UserDTO;
import hu.bme.compsec.sudoku.authserver.presentation.mapping.UserMapper;
import hu.bme.compsec.sudoku.common.security.UserRole;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

import static hu.bme.compsec.sudoku.common.security.SecurityUtils.checkPermission;
import static hu.bme.compsec.sudoku.common.security.SecurityUtils.getUserIdFromJwt;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        log.trace("Checking user with username {}.", userName);
        var userEntity = userRepository.findByUsername(userName)
                .orElseThrow(() -> {
                    log.trace("There is no user with username {}.", userName);
                    return new UsernameNotFoundException("There is no user with username: " + userName);
                });
        return userMapper.toSecurityUser(userEntity);
    }


    public boolean createUser(UserDTO dto) throws UsernameAlreadyInUseException {
        checkUsernameExistence(dto);

        try {
            userRepository.save(userMapper.toEntity(dto));
            return true;
        } catch (Exception e) {
            log.error("Error while registering client with username {}: {}", dto.getUsername(), e.getMessage());
            return false;
        }
    }

    public Optional<User> getAuthenticatedUser() {
        return userRepository.findById(getUserIdFromJwt());
    }

    public boolean updateUser(UserDTO dto) throws UsernameAlreadyInUseException, UserNotFoundException {
        final Long authenticatedUserId = getUserIdFromJwt();
        var userEntity = userRepository.findById(authenticatedUserId)
                .map(user -> {
                     checkPermission(user.getId());
                    return user;
                })
                .orElseThrow(() -> new UserNotFoundException("There is no user with id {} in the db.", authenticatedUserId));

        if (!userEntity.getUsername().equals(dto.getUsername())) {
            checkUsernameExistence(dto);
        }

        try {
            var updatedEntity = userMapper.toEntity(dto);
            updatedEntity.setId(userEntity.getId());
            userRepository.save(updatedEntity);
            return true;
        } catch (Exception e) {
            log.error("Error while update client data: {}", e.getMessage());
        return false;
        }
    }

    public void forgotPassword() {
        if (userRepository.findById(getUserIdFromJwt()).isPresent()) {
            // TODO: Send mail with random and ...
        }
    }

    public void deleteUser() {
        if (userRepository.findById(getUserIdFromJwt()).isPresent()) {
            userRepository.deleteById(getUserIdFromJwt());
        }
    }

    private void checkUsernameExistence(UserProfileDTO dto) throws UsernameAlreadyInUseException {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new UsernameAlreadyInUseException("Username {} is already in use!", dto.getUsername());
        }
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

        log.info("Saving default user {}.", user);
        userRepository.save(user);
        log.info("Saving default user {}.", admin);
        userRepository.save(admin);
    }


}
