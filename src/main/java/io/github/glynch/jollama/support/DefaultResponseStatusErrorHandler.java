package io.github.glynch.jollama.support;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.glynch.jollama.JOllamaError;
import io.github.glynch.jollama.client.JOllamaClientResponseException;
import okhttp3.Response;

public class DefaultResponseStatusErrorHandler implements ResponseStatusErrorHandler {

    private final ObjectMapper objectMapper;

    public DefaultResponseStatusErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handleError(Response response) {

        try {
            throw new JOllamaClientResponseException(
                    objectMapper.readValue(response.body().string(), JOllamaError.class).error(), response.code());
        } catch (IOException e) {
            throw new JOllamaClientResponseException("Failed to parse error response", response.code());
        }
    }

}
