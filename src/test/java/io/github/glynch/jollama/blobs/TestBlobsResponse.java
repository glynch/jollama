package io.github.glynch.jollama.blobs;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.glynch.jollama.client.JOllamaClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

class TestBlobsResponse {

    static MockWebServer server;
    static JOllamaClient client;

    @BeforeAll
    static void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        String url = server.url("/api/blobs").toString();
        client = JOllamaClient.builder(url).build();
    }

    @Test
    void blobsExistsResponse() {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(200);
        server.enqueue(mockResponse);

        int statusCode = client.blobs()
                .exists("sha256:33eb43a1488dfe4da10339f40a7f1918179c804bae1ba36ae8a04052cc4f518a");

        assertEquals(200, statusCode);
    }

    @AfterAll
    static void tearDown() throws IOException {
        server.shutdown();
    }

}
