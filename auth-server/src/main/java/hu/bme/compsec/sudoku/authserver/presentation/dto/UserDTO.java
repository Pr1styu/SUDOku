package hu.bme.compsec.sudoku.authserver.presentation.dto;

import lombok.Data;

@Data
public class UserDTO {

    private String username;

    private String password;

    private String email;

    private String fullName;

}
