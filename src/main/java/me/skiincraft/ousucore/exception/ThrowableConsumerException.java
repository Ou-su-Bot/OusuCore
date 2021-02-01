package me.skiincraft.ousucore.exception;

public class ThrowableConsumerException extends RuntimeException {

    private Exception exception;

    public ThrowableConsumerException(Exception exception){
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }
}