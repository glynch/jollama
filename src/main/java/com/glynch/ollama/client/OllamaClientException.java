package com.glynch.ollama.client;

public class OllamaClientException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OllamaClientException(String message) {
        super(message);
    }

    public OllamaClientException(String message, Throwable cause) {
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