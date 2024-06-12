package com.glynch.jollama.client;

import java.net.URI;

public class JOllamaClientRequestException extends JOllamaClientException {

    private static final long serialVersionUID = 1L;

    private final URI uri;
    private final String method;

    public JOllamaClientRequestException(String message, URI uri, String method) {
        this(message, null, uri, method);

    }

    public JOllamaClientRequestException(String message, Throwable cause, URI uri, String method) {
        super(message, cause);
        this.uri = uri;
        this.method = method;
    }

    public URI getUri() {
        return uri;
    }

    public String getMethod() {
        return method;
    }

}
