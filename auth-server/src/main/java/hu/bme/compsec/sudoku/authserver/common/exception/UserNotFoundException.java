package hu.bme.compsec.sudoku.authserver.common.exception;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(String format, Object args) {
        super(String.format(format, args));
    }

}
