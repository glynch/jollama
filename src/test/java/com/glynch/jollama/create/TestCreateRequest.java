package com.glynch.jollama.create;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestCreateRequest {

    @Test
    public void testCreateRequest() {

        String modelFile = "FROM llaam3\nSYSTEM";
        CreateRequest request = new CreateRequest("glynch/llama3", modelFile, true, null);

        assertAll(
                () -> assertEquals("glynch/llama3", request.name()),
                () -> assertEquals(modelFile, request.modelfile()),
                () -> assertEquals(true, request.stream()),
                () -> assertEquals(null, request.path()));

    }

}
