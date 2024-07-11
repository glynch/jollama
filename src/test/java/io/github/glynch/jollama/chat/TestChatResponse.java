package io.github.glynch.jollama.chat;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.glynch.jollama.Model;
import io.github.glynch.jollama.chat.history.MessageHistory;
import io.github.glynch.jollama.client.JOllamaClient;
import io.github.glynch.jollama.client.JOllamaClientRequestException;
import io.github.glynch.jollama.client.JOllamaClientResponseException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.SocketPolicy;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class TestChatResponse {

    static MockWebServer server;
    static JOllamaClient client;

    @BeforeAll
    static void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        String url = server.url("/api/chat").toString();
        client = JOllamaClient.builder(url).build();
    }

    @Test
    void batchChatResponse() throws IOException {
        MockResponse mockResponse = new MockResponse();
        String json = Files.readString(Path.of("src/test/resources/responses/chat/batch.json"));
        mockResponse.setBody(json);
        server.enqueue(mockResponse);
        ChatResponse chatResponse = client.chat(Model.LLAMA_3_LATEST, "What is the capital of Australia?").batch();
        assertAll(
                () -> assertEquals(chatResponse.model(), "llama3"),
                () -> assertEquals("The capital of Australia is Canberra.",
                        chatResponse.message().content()),
                () -> assertEquals(Role.ASSISTANT, chatResponse.message().role()),
                () -> assertEquals(Instant.parse("2024-07-08T22:42:47.753936Z"),
                        chatResponse.createdAt()),
                () -> assertEquals("stop", chatResponse.doneReason()),
                () -> assertTrue(chatResponse.done()),
                () -> assertEquals(2522590708L, chatResponse.totalDuration()),
                () -> assertEquals(2293286875L, chatResponse.loadDuration()),
                () -> assertEquals(18L, chatResponse.promptEvalCount()),
                () -> assertEquals(97960000L, chatResponse.promptEvalDuration()),
                () -> assertEquals(8L, chatResponse.evalCount()),
                () -> assertEquals(129317000L, chatResponse.evalDuration()));

    }

    @Test
    void chatResponseBatchWithHistory() throws IOException {
        MockResponse mockResponse = new MockResponse();
        String json = Files.readString(Path.of("src/test/resources/responses/chat/batch.json"));
        mockResponse.setBody(json);
        server.enqueue(mockResponse);
        MessageHistory history = MessageHistory.create();
        client.chat(Model.LLAMA_3_LATEST, "What is the capital of Australia?")
                .system("You are a geographical expert").history(history).batch();

        assertAll(
                () -> assertEquals("You are a geographical expert", history.system().get().content()),
                () -> assertEquals("What is the capital of Australia?", history.get(0).content()),
                () -> assertEquals("The capital of Australia is Canberra.", history.get(1).content()));

    }

    @Test
    void throwsException404() throws IOException {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setResponseCode(404);
        String json = Files.readString(Path.of("src/test/resources/responses/chat/404-error.json"));
        mockResponse.setBody(json);
        server.enqueue(mockResponse);
        JOllamaClientResponseException exception = assertThrows(JOllamaClientResponseException.class,
                () -> client.chat("llama31", "Why is the sky blue?").batch());

        assertEquals(404, exception.getStatusCode());
    }

    @Test
    void streamChatResponse() throws IOException {
        MockResponse mockResponse = new MockResponse();
        String json = Files.readString(Path.of("src/test/resources/responses/chat/stream.json"));
        mockResponse.setBody(json);
        server.enqueue(mockResponse);
        Flux<ChatResponse> response = client.chat(Model.LLAMA_3_LATEST, "What is the capital of Australia?").stream();
        StepVerifier.create(response)
                .assertNext(
                        r -> assertEquals("The", r.message().content()))
                .assertNext(
                        r -> assertEquals(" capital", r.message().content()))
                .assertNext(
                        r -> assertEquals(" of", r.message().content()))
                .assertNext(
                        r -> assertEquals(" Australia", r.message().content()))
                .assertNext(
                        r -> assertEquals(" is", r.message().content()))
                .assertNext(
                        r -> assertEquals(" Canberra", r.message().content()))
                .assertNext(
                        r -> assertEquals(".", r.message().content()))
                .assertNext(r -> assertAll(
                        () -> assertEquals("llama3", r.model()),
                        () -> assertEquals("", r.message().content()),
                        () -> assertEquals(Role.ASSISTANT, r.message().role()),
                        () -> assertEquals(Instant.parse("2024-07-08T21:46:56.598562Z"), r.createdAt()),
                        () -> assertEquals("stop", r.doneReason()),
                        () -> assertTrue(r.done()),
                        () -> assertEquals(282146417L, r.totalDuration()),
                        () -> assertEquals(2215375L, r.loadDuration()),
                        () -> assertEquals(18L, r.promptEvalCount()),
                        () -> assertEquals(155181000L, r.promptEvalDuration()),
                        () -> assertEquals(8L, r.evalCount()),
                        () -> assertEquals(123235000L, r.evalDuration())))

                .verifyComplete();

    }

    @Test
    void chatResponseStreamhWithHistory() throws IOException {
        MockResponse mockResponse = new MockResponse();
        String json = Files.readString(Path.of("src/test/resources/responses/chat/stream.json"));
        mockResponse.setBody(json);
        server.enqueue(mockResponse);
        MessageHistory history = MessageHistory.create();
        client.chat(Model.LLAMA_3_LATEST, "What is the capital of Australia?")
                .system("You are a geographical expert").history(history).stream().subscribe();

        assertAll(
                () -> assertEquals("You are a geographical expert",
                        history.system().get().content()),
                () -> assertEquals("What is the capital of Australia?",
                        history.get(0).content()),
                () -> assertEquals("The capital of Australia is Canberra.",
                        history.get(1).content()));

    }

    @Test
    void slowResponse() {
        MockResponse mockResponse = new MockResponse();
        mockResponse.setSocketPolicy(SocketPolicy.NO_RESPONSE);
        server.enqueue(mockResponse);

        assertThrows(
                JOllamaClientRequestException.class,
                () -> client.chat("llama3", "What is the capital of Australia?").batch());

    }

    @AfterAll
    static void tearDown() throws IOException {
        server.shutdown();
    }

}
