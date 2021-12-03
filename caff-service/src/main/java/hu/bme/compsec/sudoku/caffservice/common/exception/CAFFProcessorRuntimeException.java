package hu.bme.compsec.sudoku.caffservice.common.exception;

public class CAFFProcessorRuntimeException extends Exception{
    public CAFFProcessorRuntimeException(String format, Object... args) {
        super(String.format(format.replace("{}", "%s"), args));
    }
}
