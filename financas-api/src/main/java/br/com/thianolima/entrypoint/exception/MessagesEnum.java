package br.com.thianolima.entrypoint.exception;

public enum MessagesEnum {
    EXCEPTION_GENERIC("exception.generic"),
    USER_NOT_FOUND("user.not.found"),
    USER_UNIQUE_KEY("user.unique.key"),
    TOKEN_INVALID("token.invalid"),
    TOKEN_FORBIDDEN("token.forbidden");

    private final String key;

    MessagesEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
