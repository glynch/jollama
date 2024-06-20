package com.glynch.jollama.client;

public class JOllamaClientResponseException extends JOllamaClientException {

    private final int statusCode;

    public JOllamaClientResponseException(String message, int statusCode) {
        this(message, null, statusCode);
    }

    public JOllamaClientResponseException(String message, Throwable cause, int statusCode) {
        super(message, cause);
        this.statusCode = statusCode;

    }

    public int getStatusCode() {
        return statusCode;
    }

}
