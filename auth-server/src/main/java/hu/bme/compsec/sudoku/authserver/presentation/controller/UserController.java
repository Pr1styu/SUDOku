package hu.bme.compsec.sudoku.authserver.presentation.controller;

import hu.bme.compsec.sudoku.authserver.presentation.dto.UserDTO;
import hu.bme.compsec.sudoku.authserver.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserDTO dto) {
         if (userService.createUser(dto)) {
            return ResponseEntity.ok().build();
         } else {
             return ResponseEntity.badRequest().build();
         }
    }

    // TODO: Figure out these
    public ResponseEntity forgotPassword() {
        throw new RuntimeException("Not yet implemented!");
    }

    public ResponseEntity deleteUser() {
        throw new RuntimeException("Not yet implemented!");
    }

}
