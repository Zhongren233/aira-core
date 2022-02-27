package moe.aira.exception;

public class AiraIllegalParamsException extends AiraException {
    public AiraIllegalParamsException() {
        super();
    }

    public AiraIllegalParamsException(String message) {
        super(message);
    }

    public AiraIllegalParamsException(String message, Throwable cause) {
        super(message, cause);
    }

    public AiraIllegalParamsException(Throwable cause) {
        super(cause);
    }

    protected AiraIllegalParamsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public Integer errorCode() {
        return 401;
    }
}
