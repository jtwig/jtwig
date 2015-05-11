package org.jtwig.exception;

public class JtwigException extends Exception {
    public JtwigException() {
    }

    public JtwigException(String message) {
        super(message);
    }

    public JtwigException(String message, Throwable cause) {
        super(message, cause);
    }

    public JtwigException(Throwable cause) {
        super(cause);
    }

    public JtwigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
