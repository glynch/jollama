package com.glynch.jollama.modelfile;

import com.glynch.jollama.NestedRuntimeException;

public class InvalidModelFileException extends NestedRuntimeException {

    public InvalidModelFileException(String message) {
        super(message);
    }

}
