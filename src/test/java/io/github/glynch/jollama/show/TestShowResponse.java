package io.github.glynch.jollama.show;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import io.github.glynch.jollama.client.JOllamaClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

class TestShowResponse {

    static MockWebServer server;
    static JOllamaClient client;

    @BeforeAll
    static void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        String url = server.url("/api/show").toString();
        client = JOllamaClient.create(url);
    }

    @Test
    void showResponse() throws IOException {
        MockResponse mockResponse = new MockResponse();
        String json = Files.readString(Path.of("src/test/resources/responses/show/show.json"));
        mockResponse.setBody(json);
        server.enqueue(mockResponse);

        ShowResponse showResponse = client.show("phi3");
        assertAll(
                () -> assertEquals("phi3", showResponse.details().family()),
                () -> assertEquals(List.of("phi3"), showResponse.details().families()),
                () -> assertEquals("gguf", showResponse.details().format()),
                () -> assertEquals("3.8B", showResponse.details().parameterSize()),
                () -> assertEquals("Q4_K_M", showResponse.details().quantizationLevel()),
                () -> assertEquals("phi3", showResponse.modelInfo().generalArchitecture()));
    }

    @AfterAll
    static void tearDown() throws IOException {
        server.shutdown();
    }

}
