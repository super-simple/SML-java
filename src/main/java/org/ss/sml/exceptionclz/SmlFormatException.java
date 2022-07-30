package org.ss.sml.exceptionclz;

public class SmlFormatException extends RuntimeException {
    public SmlFormatException() {
    }

    public SmlFormatException(String message) {
        super(message);
    }

    public SmlFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmlFormatException(Throwable cause) {
        super(cause);
    }

    public SmlFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
