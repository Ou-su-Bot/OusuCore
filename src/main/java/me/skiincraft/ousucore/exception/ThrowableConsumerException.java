package me.skiincraft.ousucore.exception;

public class ThrowableConsumerException extends RuntimeException {

    private Throwable throwable;

    public ThrowableConsumerException(Throwable cause) {
        super(cause);
        this.throwable = cause;
    }

    public Exception getException() {
        return (throwable instanceof Exception) ? (Exception) throwable : this;
    }
}
