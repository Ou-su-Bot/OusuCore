package me.skiincraft.ousucore.exception;

public class BotLoaderException extends RuntimeException {

    private int code;

    public BotLoaderException(String message, int code) {
        super(message);
        this.code = code;
    }

    public BotLoaderException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
