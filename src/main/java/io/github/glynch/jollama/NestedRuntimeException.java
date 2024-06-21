package io.github.glynch.jollama;

public class NestedRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NestedRuntimeException(String message) {
        super(message);
    }

    public NestedRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public Throwable getRootCause() {
        Throwable cause = getCause();
        if (cause == null) {
            return this;
        }
        Throwable nextCause = cause;
        while ((nextCause = nextCause.getCause()) != null) {
            cause = nextCause;
        }
        return cause;
    }

    public Throwable getMostSpecificCause() {
        Throwable rootCause = getRootCause();
        return (rootCause != null ? rootCause : this);
    }
}
