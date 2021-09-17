package lv.cebbys.mcmods.celib.processor.exception;

public class ValidationException extends Exception {
    public ValidationException() {
    }
    public ValidationException(String cause) {
        super(cause);
    }
    public ValidationException(String cause, Throwable e) {
        super(cause, e);
    }
}
