package hu.bme.compsec.sudoku.caffservice.common.exception;

public class CaffFileFormatException extends Exception {

    public CaffFileFormatException(String format, Object... args) {
        super(String.format(format.replace("{}", "%s"), args));
    }

}
