package org.ss.smljava.exceptionclz;

public class SmlParseException extends RuntimeException {
    public SmlParseException() {
    }

    public SmlParseException(String message) {
        super(message);
    }

    public SmlParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmlParseException(Throwable cause) {
        super(cause);
    }

    public SmlParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
