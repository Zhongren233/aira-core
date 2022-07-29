package moe.aira.exception.client;

import moe.aira.exception.AiraException;

public class AiraIllegalParamsException extends AiraException {
    public AiraIllegalParamsException() {
        super("不正确的参数");
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
