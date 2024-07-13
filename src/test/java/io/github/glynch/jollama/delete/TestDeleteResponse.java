package io.github.glynch.jollama.delete;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.glynch.jollama.Model;
import io.github.glynch.jollama.client.JOllamaClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

class TestDeleteResponse {

    static MockWebServer server;
    static JOllamaClient client;

    @BeforeAll
    static void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        String url = server.url("/api/delete").toString();
        client = JOllamaClient.builder(url).build();
    }

    @Test
    void deleteResponse() {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(200);
        server.enqueue(mockResponse);

        int statusCode = client.delete(Model.LLAMA_3_LATEST);

        assertEquals(200, statusCode);

    }

    @Test
    void deleteResponse404() {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(404);
        server.enqueue(mockResponse);

        int statusCode = client.delete("dummy");

        assertEquals(404, statusCode);

    }

    @AfterAll
    static void tearDown() throws IOException {
        server.shutdown();
    }

}
