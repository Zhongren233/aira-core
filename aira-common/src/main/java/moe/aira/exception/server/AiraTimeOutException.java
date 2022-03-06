package moe.aira.exception.server;

import moe.aira.exception.AiraException;

public class AiraTimeOutException extends AiraException {
    public AiraTimeOutException() {
    }

    public AiraTimeOutException(String message) {
        super(message);
    }

    public AiraTimeOutException(String message, Throwable cause) {
        super(message, cause);
    }

    public AiraTimeOutException(Throwable cause) {
        super(cause);
    }

    protected AiraTimeOutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public Integer errorCode() {
        return 501;
    }
}
