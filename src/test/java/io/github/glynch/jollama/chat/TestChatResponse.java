package io.github.glynch.jollama.chat;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.glynch.jollama.client.JOllamaClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

class TestChatResponse {

    MockWebServer server;
    JOllamaClient client;

    @BeforeEach
    void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        String url = server.url("/api/chat").toString();
        client = JOllamaClient.create(url);
    }

    @Test
    void test() throws IOException {
        MockResponse response = new MockResponse();
        String json = Files.readString(Path.of("src/test/resources/responses/chat/batch.json"));
        response.setBody(json);
        server.enqueue(response);
        ChatResponse chatResponse = client.chat("llama3", "Why is the sky blue?").batch();
        assertAll(
                () -> assertTrue(chatResponse.message().content().contains("A classic question")),
                () -> assertEquals(chatResponse.model(), "llama3"),
                () -> assertEquals(chatResponse.message().role(), Role.ASSISTANT),
                () -> assertEquals(chatResponse.evalCount(), 395L),
                () -> assertEquals(chatResponse.promptEvalDuration(), 105731000L),
                () -> assertEquals(chatResponse.evalDuration(), 7285219000L),
                () -> assertEquals(chatResponse.promptEvalCount(), 17L),
                () -> assertEquals(chatResponse.doneReason(), "stop"),
                () -> assertTrue(chatResponse.done()));

    }

    @AfterEach
    void tearDown() throws IOException {
        server.shutdown();
    }

}
