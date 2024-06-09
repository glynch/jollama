package com.glynch.ollama.modelfile;

import com.glynch.ollama.NestedRuntimeException;

public class InvalidModelFileException extends NestedRuntimeException {

    public InvalidModelFileException(String message) {
        super(message);
    }

    public InvalidModelFileException(String message, Throwable cause) {
        super(message, cause);
    }

}
