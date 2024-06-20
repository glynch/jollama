package com.glynch.jollama.client;

import com.glynch.jollama.NestedRuntimeException;

public class JOllamaClientException extends NestedRuntimeException {

    private static final long serialVersionUID = 1L;

    public JOllamaClientException(String message) {
        this(message, null);
    }

    public JOllamaClientException(String message, Throwable cause) {
        super(message, cause);
    }

}