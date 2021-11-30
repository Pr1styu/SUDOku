package hu.bme.compsec.sudoku.authserver.service;

import hu.bme.compsec.sudoku.authserver.common.UserRole;
import hu.bme.compsec.sudoku.authserver.common.exception.UserNotFoundException;
import hu.bme.compsec.sudoku.authserver.common.exception.UsernameAlreadyInUseException;
import hu.bme.compsec.sudoku.authserver.data.User;
import hu.bme.compsec.sudoku.authserver.data.UserRepository;
import hu.bme.compsec.sudoku.authserver.presentation.dto.UserDTO;
import hu.bme.compsec.sudoku.authserver.presentation.mapping.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    private Long getAuthenticatedUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            var jwt = (Jwt) auth.getPrincipal();
            return jwt.getClaim("user_id"); // TODO: Extract user_id string to commons
        } catch (Exception e) {
            log.error("Cannot parse JWT due to: {}", e.getMessage());
            throw new InvalidBearerTokenException("Could NOT get user id from JWT.");
        }
    }

    // TODO: Use SecurityUtils instead of these
    public Optional<User> getAuthenticatedUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            var jwt = (Jwt) auth.getPrincipal();
            var jwtUserId = (Long) jwt.getClaim("user_id");
            return userRepository.findById(jwtUserId);
        } catch (Exception e) {
            log.error("Cannot parse JWT due to: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public boolean updateUser(UserDTO dto) throws UsernameAlreadyInUseException, UserNotFoundException {

        final Long authenticatedUserId = getAuthenticatedUserId();
        var userEntity = userRepository.findById(authenticatedUserId)
                .map(user -> {
                    checkPermissionForUserId(user.getId());
                    return user;
                })
                .orElseThrow(() -> new UserNotFoundException("There is no user with authenticatedUserId in the db: {}", authenticatedUserId));

        if (!userEntity.getUsername().equals(dto.getUsername())) {
            if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
                throw new UsernameAlreadyInUseException("Username {} is already in use.", dto.getUsername());
            }
        }

        try {
            var updatedEntity = userMapper.toEntity(dto);
            updatedEntity.setId(userEntity.getId());
            userRepository.save(updatedEntity);
            return true;
        } catch (Exception e) {
            log.error("Error while update client data: {}", e.getMessage());
        }

        return false;
    }

    private void checkPermissionForUserId(Long userId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            var jwt = (Jwt) auth.getPrincipal();
            var jwtUserId = (Long) jwt.getClaim("user_id");

            if (!Objects.equals(userId, jwtUserId)) {
                throw new InvalidBearerTokenException(
                        String.format("User with id {} not have the right permission for editing user with id {}.", jwtUserId, userId)
                );
            }
        } catch (Exception e) {
            log.error("Cannot parse JWT due to: {}", e.getMessage());
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

        userRepository.save(user);
        log.info("Saving default user {}.", user);
        log.info("Saving default user {}.", admin);
        userRepository.save(admin);
    }


}
