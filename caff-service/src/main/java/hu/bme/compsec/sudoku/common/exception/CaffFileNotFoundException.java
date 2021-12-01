package hu.bme.compsec.sudoku.common.exception;

public class CaffFileNotFoundException extends Exception {

    public CaffFileNotFoundException(String msg, Object... args) {
        super(String.format(msg, args));
    }

}