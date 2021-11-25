package hu.bme.compsec.sudoku.common.exception;

public class CaffFileFormatException extends Exception {

    public CaffFileFormatException(String msg, Object... args) {
        super(String.format(msg, args));
    }

}
