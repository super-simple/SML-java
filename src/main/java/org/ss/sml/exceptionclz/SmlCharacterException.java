package org.ss.sml.exceptionclz;

public class SmlCharacterException extends RuntimeException {
    public SmlCharacterException() {
    }

    public SmlCharacterException(String message) {
        super(message);
    }

    public SmlCharacterException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmlCharacterException(Throwable cause) {
        super(cause);
    }

    public SmlCharacterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
