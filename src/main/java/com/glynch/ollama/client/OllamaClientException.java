package com.glynch.ollama.client;

import com.glynch.ollama.NestedRuntimeException;

public class OllamaClientException extends NestedRuntimeException {

    private static final long serialVersionUID = 1L;

    public OllamaClientException(String message) {
        super(message);
    }

    public OllamaClientException(String message, Throwable cause) {
        super(message, cause);
    }

}