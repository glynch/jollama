package com.glynch.ollama.client;

import java.net.URI;

public class OllamaClientRequestException extends OllamaClientException {

    private static final long serialVersionUID = 1L;

    private final URI uri;
    private final String method;

    public OllamaClientRequestException(String message, URI uri, String method) {
        this(message, null, uri, method);

    }

    public OllamaClientRequestException(String message, Throwable cause, URI uri, String method) {
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
