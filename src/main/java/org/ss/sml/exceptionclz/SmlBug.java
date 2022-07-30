package org.ss.sml.exceptionclz;

public class SmlBug extends RuntimeException {
    public SmlBug() {
    }

    public SmlBug(String message) {
        super(message);
    }

    public SmlBug(String message, Throwable cause) {
        super(message, cause);
    }

    public SmlBug(Throwable cause) {
        super(cause);
    }

    public SmlBug(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
