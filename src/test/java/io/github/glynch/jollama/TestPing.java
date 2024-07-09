package io.github.glynch.jollama;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.glynch.jollama.client.JOllamaClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.SocketPolicy;

class TestPing {

    static MockWebServer server;
    static JOllamaClient client;

    @BeforeAll
    static void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        String url = server.url("").toString();
        client = JOllamaClient.create(url);
    }

    @Test
    void pingIsUp() {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(200);
        server.enqueue(mockResponse);
        boolean isUp = client.ping();

        assertTrue(isUp);

    }

    @Test
    void pingIsDown() {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setSocketPolicy(SocketPolicy.NO_RESPONSE);
        server.enqueue(mockResponse);
        boolean isUp = client.ping();

        assertTrue(!isUp);

    }

    @AfterAll
    static void tearDown() throws IOException {
        server.shutdown();
    }

}
