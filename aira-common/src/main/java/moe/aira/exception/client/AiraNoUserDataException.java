package moe.aira.exception.client;

import moe.aira.exception.AiraException;

public class AiraNoUserDataException extends AiraException {
    public AiraNoUserDataException() {
        super("没有对应用户信息");
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
