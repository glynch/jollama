package io.github.glynch.jollama.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import org.junit.jupiter.api.Test;

public class TestJOllamaClientBuilder {

    @Test
    void builderWithDefaultHost() {
        JOllamaClient client = JOllamaClient.builder()
                .build();

        assertEquals(JOllamaClient.DEFAULT_OLLAMA_HOST, client.getHost());
    }

    @Test
    void builderWithValidHost() {
        JOllamaClient client = JOllamaClient.builder("http://localhost:8080")
                .build();

        assertEquals("http://localhost:8080", client.getHost());
    }

    @Test
    void createWithDefaultHost() {
        JOllamaClient client = JOllamaClient.create();

        assertEquals(JOllamaClient.DEFAULT_OLLAMA_HOST, client.getHost());
    }

    @Test
    void createWithValidHost() {
        JOllamaClient client = JOllamaClient.create("http://localhost:8080");

        assertEquals("http://localhost:8080", client.getHost());
    }

    @Test
    void builderConnectTimeout() {
        JOllamaClient client = JOllamaClient.builder().connectTimeout(Duration.ofSeconds(5))
                .build();

        assertEquals(5000, client.getConnectTimeout());
    }

    @Test
    void builderReadTimeout() {
        JOllamaClient client = JOllamaClient.builder().readTimeout(Duration.ofSeconds(5))
                .build();

        assertEquals(5000, client.getReadTimeout());
    }

    @Test
    void builderFollowRedirects() {
        JOllamaClient client = JOllamaClient.builder().followRedirects()
                .build();

        assertEquals(Redirect.NORMAL, client.getRedirect());
    }

    @Test
    void builderFollowRedirectsNever() {
        JOllamaClient client = JOllamaClient.builder().followRedirectsNever()
                .build();

        assertEquals(Redirect.NEVER, client.getRedirect());
    }

    @Test
    void builderFollowRedirectsAlways() {
        JOllamaClient client = JOllamaClient.builder().followRedirectsAlways()
                .build();

        assertEquals(Redirect.ALWAYS, client.getRedirect());
    }

}
