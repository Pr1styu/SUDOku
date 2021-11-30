package hu.bme.compsec.sudoku.authserver.common.exception;

public class UsernameAlreadyInUseException extends Exception {

    public UsernameAlreadyInUseException(String format, Object... args) {
        super(String.format(format.replace("{}", "%s"), args));
    }

}
