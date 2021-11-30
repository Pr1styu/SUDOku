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

@Slf4j
@RestController
@RequestMapping("/")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserDTO dto) {
         if (userService.createUser(dto)) {
            return ResponseEntity.ok().build();
         } else {
             return ResponseEntity.badRequest().build();
         }
    }

    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDTO> getUserProfile() {
        return userService.getAuthenticatedUser()
                .map(userMapper::toProfileDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity updateUserData(@RequestBody UserDTO dto) {
        log.trace("User {} about to modify user data: {}", dto.getUsername(), dto);

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

    // TODO: Figure out these
    public ResponseEntity forgotPassword() {

        throw new RuntimeException("Not yet implemented!");
    }

    public ResponseEntity deleteUserAccount() {
        throw new RuntimeException("Not yet implemented!");
    }

}
