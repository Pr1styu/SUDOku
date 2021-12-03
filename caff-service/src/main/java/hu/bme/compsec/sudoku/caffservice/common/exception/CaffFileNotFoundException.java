package hu.bme.compsec.sudoku.caffservice.common.exception;

public class CaffFileNotFoundException extends Exception {

    public CaffFileNotFoundException(String format, Object... args) {
        super(String.format(format.replace("{}", "%s"), args));
    }

}