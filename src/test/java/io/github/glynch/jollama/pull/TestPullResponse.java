package io.github.glynch.jollama.pull;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.glynch.jollama.Model;
import io.github.glynch.jollama.client.JOllamaClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class TestPullResponse {

    static MockWebServer server;
    static JOllamaClient client;

    @BeforeAll
    static void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        String url = server.url("/api/pull").toString();
        client = JOllamaClient.builder(url).build();
    }

    @Test
    void pullResponseBatch() throws IOException {
        MockResponse mockResponse = new MockResponse();
        String json = Files.readString(Path.of("src/test/resources/responses/pull/batch.json"));
        mockResponse.setBody(json);
        server.enqueue(mockResponse);

        PullResponse pullResponse = client.pull(Model.ORCA_MINI_LATEST).batch();
        assertEquals("success", pullResponse.status());

    }

    @Test
    void pullResponseStream() throws IOException {
        MockResponse mockResponse = new MockResponse();
        String json = Files.readString(Path.of("src/test/resources/responses/pull/stream.json"));
        mockResponse.setBody(json);
        server.enqueue(mockResponse);

        Flux<PullResponse> pullResponse = client.pull(Model.ORCA_MINI_LATEST).stream();
        StepVerifier.create(pullResponse)
                .expectNextCount(696).verifyComplete();

    }

    @AfterAll
    static void tearDown() throws IOException {
        server.shutdown();
    }

}
