package io.github.glynch.jollama.modelfile;

import io.github.glynch.jollama.NestedRuntimeException;

public class InvalidModelFileException extends NestedRuntimeException {

    public InvalidModelFileException(String message) {
        super(message);
    }

}
