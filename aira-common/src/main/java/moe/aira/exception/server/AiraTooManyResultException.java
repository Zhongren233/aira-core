package moe.aira.exception.server;

import moe.aira.exception.AiraException;

public class AiraTooManyResultException extends AiraException {
    public AiraTooManyResultException() {
    }

    public AiraTooManyResultException(String message) {
        super(message);
    }

    public AiraTooManyResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public AiraTooManyResultException(Throwable cause) {
        super(cause);
    }

    protected AiraTooManyResultException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public Integer errorCode() {
        return 502;
    }
}
