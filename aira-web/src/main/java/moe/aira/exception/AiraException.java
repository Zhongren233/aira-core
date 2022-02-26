package moe.aira.exception;

public class AiraException extends RuntimeException {
    public AiraException() {
        super();
    }

    public AiraException(String message) {
        super(message);
    }

    public AiraException(String message, Throwable cause) {
        super(message, cause);
    }

    public AiraException(Throwable cause) {
        super(cause);
    }

    protected AiraException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
