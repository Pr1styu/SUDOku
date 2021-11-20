package hu.bme.compsec.sudoku.common.exception;

public class CaffFileFormatExpression extends Exception {

    public CaffFileFormatExpression(String msg, Object... args) {
        super(String.format(msg, args));
    }

}
