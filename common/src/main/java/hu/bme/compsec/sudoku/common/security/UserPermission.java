package hu.bme.compsec.sudoku.common.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserPermission {

    CAFF_READ("caff:read"),
    CAFF_WRITE("caff:write"),
    CAFF_DELETE("caff:delete");


    private final String permissionText;

}