package moe.aira.exception.server;

import moe.aira.exception.AiraException;

public class AiraNotOpenEventException extends AiraException {
    public AiraNotOpenEventException() {
        super("非活动状态");
    }

    public AiraNotOpenEventException(String message) {
        super(message);
    }

    public AiraNotOpenEventException(String message, Throwable cause) {
        super(message, cause);
    }

    public AiraNotOpenEventException(Throwable cause) {
        super(cause);
    }

    protected AiraNotOpenEventException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public Integer errorCode() {
        return 503;
    }
}
