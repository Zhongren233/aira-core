package moe.aira.exception;

public class EnsembleStarsException extends AiraException {
    public EnsembleStarsException() {
        super();
    }

    public EnsembleStarsException(String message) {
        super(message);
    }

    public EnsembleStarsException(String message, Throwable cause) {
        super(message, cause);
    }

    public EnsembleStarsException(Throwable cause) {
        super(cause);
    }

    protected EnsembleStarsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
