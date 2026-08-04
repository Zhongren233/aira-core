package moe.aira.onebot.client.skyland;

public class SkylandApiException extends RuntimeException {

    public SkylandApiException(String message) {
        super(message);
    }

    public SkylandApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
