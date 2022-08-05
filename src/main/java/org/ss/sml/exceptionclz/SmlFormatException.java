package org.ss.sml.exceptionclz;

public class SmlFormatException extends RuntimeException {

    private int index;
    private char currentChar;

    public SmlFormatException(int index, char currentChar) {
        this.index = index;
        this.currentChar = currentChar;
    }

    public SmlFormatException(String message, int index, char currentChar) {
        super(message);
        this.index = index;
        this.currentChar = currentChar;
    }

    public SmlFormatException(String message, Throwable cause, int index, char currentChar) {
        super(message, cause);
        this.index = index;
        this.currentChar = currentChar;
    }

    public SmlFormatException(Throwable cause, int index, char currentChar) {
        super(cause);
        this.index = index;
        this.currentChar = currentChar;
    }

    public SmlFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int index, char currentChar) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.index = index;
        this.currentChar = currentChar;
    }

    public char getCurrentChar() {
        return currentChar;
    }

    public int getIndex() {
        return index;
    }

}
