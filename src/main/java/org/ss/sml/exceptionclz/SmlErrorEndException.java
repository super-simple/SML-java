package org.ss.sml.exceptionclz;

public class SmlErrorEndException extends RuntimeException {
    public SmlErrorEndException() {
    }

    public SmlErrorEndException(String message) {
        super(message);
    }

    public SmlErrorEndException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmlErrorEndException(Throwable cause) {
        super(cause);
    }

    public SmlErrorEndException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
