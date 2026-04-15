package br.com.thianolima.entrypoint.exception;

public class DomainException extends RuntimeException {

    private MessagesEnum messageKey;
    private Object[] args;

    public DomainException(MessagesEnum messageKey, Object... args) {
        super(messageKey.getKey());
        this.messageKey = messageKey;
        this.args = args;
    }

    public DomainException() {
    }

    public MessagesEnum getMessageKey() {
        return messageKey;
    }

    public Object[] getArgs() {
        return args;
    }
}
