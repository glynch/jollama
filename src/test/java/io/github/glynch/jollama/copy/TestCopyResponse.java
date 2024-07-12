package io.github.glynch.jollama.copy;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.glynch.jollama.Model;
import io.github.glynch.jollama.client.JOllamaClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

class TestCopyResponse {

    static MockWebServer server;
    static JOllamaClient client;

    @BeforeAll
    static void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        String url = server.url("/api/copy").toString();
        client = JOllamaClient.builder(url).build();
    }

    @Test
    void copyResponse() {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(200);
        server.enqueue(mockResponse);
        int statusCode = client.copy(Model.PHI_3_LATEST, "mario-test");

        assertEquals(200, statusCode);
    }

    @Test
    void copyResponse404() {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("{\"error\":\"model \"phi31:latest\" not found\"}");
        mockResponse.setResponseCode(404);
        server.enqueue(mockResponse);

        int statusCode = client.copy("phi31:latest", "mario-test");
        assertEquals(404, statusCode);

    }

    @AfterAll
    static void tearDown() throws IOException {
        server.shutdown();
    }

}
