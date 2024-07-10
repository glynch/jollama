package io.github.glynch.jollama.generate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.glynch.jollama.client.JOllamaClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

class TestGenerateResponse {

    static MockWebServer server;
    static JOllamaClient client;

    @BeforeAll
    static void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        String url = server.url("/api/generate").toString();
        client = JOllamaClient.create(url);
    }

    @Test
    void batchGenerateResponse() throws IOException {
        MockResponse mockResponse = new MockResponse();
        String json = Files.readString(Path.of("src/test/resources/responses/generate/batch.json"));
        mockResponse.setBody(json);
        server.enqueue(mockResponse);
        GenerateResponse generateResponse = client.generate("llama3", "What is the capital of Australia?").batch();
        List<Integer> context = List.of(128006,
                882,
                128007,
                198,
                198,
                3923,
                374,
                220,
                279,
                6864,
                315,
                8494,
                30,
                128009,
                128006,
                78191,
                128007,
                198,
                198,
                791,
                6864,
                315,
                8494,
                374,
                69890,
                13,
                128009);
        assertAll(
                () -> assertEquals(generateResponse.model(), "llama3"),
                () -> assertEquals("The capital of Australia is Canberra.", generateResponse.response()),
                () -> assertEquals(Instant.parse("2024-07-10T12:14:08.867224Z"), generateResponse.createdAt()),
                () -> assertEquals("stop", generateResponse.doneReason()),
                () -> assertEquals(context, generateResponse.context()),
                () -> assertTrue(generateResponse.done()),
                () -> assertEquals(7091307084L, generateResponse.totalDuration()),
                () -> assertEquals(6837919334L, generateResponse.loadDuration()),
                () -> assertEquals(19L, generateResponse.promptEvalCount()),
                () -> assertEquals(116921000L, generateResponse.promptEvalDuration()),
                () -> assertEquals(8L, generateResponse.evalCount()),
                () -> assertEquals(128887000L, generateResponse.evalDuration()));

    }

    @AfterAll
    static void tearDown() throws IOException {
        server.shutdown();
    }

}
