package com.glynch.ollama.support;

import java.io.UncheckedIOException;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandler;
import java.util.function.Function;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.glynch.ollama.OllamaError;
import com.glynch.ollama.client.OllamaClientException;

public class Body {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final JavaTimeModule javaTimeModule = new JavaTimeModule();
    static {
        objectMapper.registerModule(javaTimeModule);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    private Body() {
    }

    public static class Handlers {

        private Handlers() {
        }

        public static <T> BodyHandler<T> of(Class<T> type) {
            return new JsonBodyHandler<>(type);
        }

        public static <T> BodyHandler<Stream<T>> streamOf(Class<T> type) {
            return new StreamJsonBodyHandler<>(type);
        }
    }

    public static class Publishers {

        private Publishers() {
        }

        public static BodyPublisher json(Object value) {
            String body;
            try {
                body = objectMapper.writeValueAsString(value);
            } catch (JsonProcessingException e) {
                throw new UncheckedIOException(e);
            }
            return BodyPublishers.ofString(body);
        }
    }

    public static class Mappers {

        private Mappers() {
        }

        public static <T> Function<String, T> map(Class<T> type) {
            return (body) -> {
                try {
                    return objectMapper.readValue(body, type);
                } catch (JsonProcessingException e) {
                    throw new UncheckedIOException(e);
                }
            };
        }

        public static <T> Function<String, T> exceptionally(Class<T> type) {
            return (body) -> {
                try {
                    throw new OllamaClientException(objectMapper.readValue(body, OllamaError.class).error());
                } catch (JsonProcessingException e) {
                    throw new OllamaClientException(body);
                }
            };
        }
    }

}
