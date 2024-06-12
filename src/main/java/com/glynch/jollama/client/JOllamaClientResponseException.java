package com.glynch.ollama.client;

import java.net.http.HttpHeaders;

public class OllamaClientResponseException extends OllamaClientException {

    private final int statusCode;
    private final HttpHeaders headers;

    public OllamaClientResponseException(String message, int statusCode, HttpHeaders headers) {
        super(message);
        this.statusCode = statusCode;
        this.headers = HttpHeaders.of(headers.map(), (k, v) -> true);
    }

    public OllamaClientResponseException(String message, Throwable cause, int statusCode, HttpHeaders headers) {
        super(message, cause);
        this.statusCode = statusCode;
        this.headers = headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

}
