package hu.bme.compsec.sudoku.authserver.presentation.controller;

import hu.bme.compsec.sudoku.authserver.common.exception.UserNotFoundException;
import hu.bme.compsec.sudoku.authserver.common.exception.UsernameAlreadyInUseException;
import hu.bme.compsec.sudoku.authserver.presentation.dto.UserDTO;
import hu.bme.compsec.sudoku.authserver.presentation.dto.UserProfileDTO;
import hu.bme.compsec.sudoku.authserver.presentation.mapping.UserMapper;
import hu.bme.compsec.sudoku.authserver.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static hu.bme.compsec.sudoku.authserver.config.SecurityUtils.getUserIdFromJwt;

@Slf4j
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600)
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO dto) {
        log.trace("Registering new user with username {}.", dto.getUsername());

        try {
            if (userService.createUser(dto)) {
               return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (UsernameAlreadyInUseException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDTO> getUserProfile() {
        log.trace("User {} about to fetching user data.", getUserIdFromJwt());

        return userService.getAuthenticatedUser()
                .map(userMapper::toProfileDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updateUserData(@RequestBody UserDTO dto) {
        log.trace("User with id {} about to modify user data to.", getUserIdFromJwt());

        try {
            if (userService.updateUser(dto)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (UsernameAlreadyInUseException | UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword() {
        userService.forgotPassword();
        return ResponseEntity.accepted().build();
    }
    //TODO Delete this or actually make a ranewpassword service
    @PostMapping("/renewPassword")
    public ResponseEntity<String> renewPassword() {
        userService.forgotPassword();
        return ResponseEntity.accepted().build();
    }

    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteUserAccount() {
        userService.deleteUser();
        return ResponseEntity.accepted().build();
    }

}
