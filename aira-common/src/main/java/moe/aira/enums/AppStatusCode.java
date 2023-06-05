package moe.aira.enums;

public enum AppStatusCode {
    OK(0),
    ClientUpdateRequired(1),
    DataUpdateRequiered(101),
    Redirect(200),
    Error(1000),
    Maintenance(1100),
    Banned(1200),
    AuthenticationError(1500),
    NetworkError(2000),
    SerializationError(2100);

    final int code;

    AppStatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
