package hu.bme.compsec.sudoku.common.exception;

public class CAFFProcessorRuntimeException extends Exception{
    public CAFFProcessorRuntimeException(String msg, Object... args) {
        super(String.format(msg, args));
    }
}
