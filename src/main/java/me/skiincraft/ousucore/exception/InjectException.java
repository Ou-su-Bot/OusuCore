package me.skiincraft.ousucore.exception;

public class InjectException extends RuntimeException {
    public InjectException(String message) {
        super(message);
    }

    public InjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public InjectException(Throwable cause) {
        super(cause);
    }
}
