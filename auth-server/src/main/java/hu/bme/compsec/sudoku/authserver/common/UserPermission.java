package hu.bme.compsec.sudoku.authserver.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserPermission {

    CAFF_READ("caff:read"),
    CAFF_WRITE("caff:write"),
    CAFF_DELETE("caff:delete");
//    DB_WRITE("db:write"),
//    DB_DELETE("db:delete");


    private final String permissionText;

}
