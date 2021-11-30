package hu.bme.compsec.sudoku.authserver.presentation.dto;

import lombok.Data;

@Data
public class UserProfileDTO {

    private String username;

    private String email;

    private String fullName;

    private String profilePicture;

}
