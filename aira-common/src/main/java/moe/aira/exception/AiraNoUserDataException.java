package moe.aira.exception;

public class AiraNoUserDataException extends AiraException {
    public AiraNoUserDataException() {
        super();
    }

    public AiraNoUserDataException(String message) {
        super(message);
    }

    public AiraNoUserDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public AiraNoUserDataException(Throwable cause) {
        super(cause);
    }

    protected AiraNoUserDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    @Override
    public Integer errorCode() {
        return 402;
    }
}
