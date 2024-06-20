package com.glynch.jollama.client;

public class JOllamaClientRequestException extends JOllamaClientException {

    private static final long serialVersionUID = 1L;

    private final String url;
    private final String method;

    public JOllamaClientRequestException(String message, String url, String method) {
        this(message, null, url, method);

    }

    public JOllamaClientRequestException(String message, Throwable cause, String url, String method) {
        super(message, cause);
        this.url = url;
        this.method = method;

    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

}
