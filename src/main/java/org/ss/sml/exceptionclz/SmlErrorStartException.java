package org.ss.sml.exceptionclz;

public class SmlErrorStartException extends RuntimeException {
    public SmlErrorStartException() {
    }

    public SmlErrorStartException(String message) {
        super(message);
    }

    public SmlErrorStartException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmlErrorStartException(Throwable cause) {
        super(cause);
    }

    public SmlErrorStartException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
