package hu.bme.compsec.sudoku.authserver.presentation.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserRegisterDTO extends UserDTO {

    private UserType userType;

}