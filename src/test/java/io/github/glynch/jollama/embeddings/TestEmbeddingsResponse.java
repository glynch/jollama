package io.github.glynch.jollama.embeddings;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.glynch.jollama.Model;
import io.github.glynch.jollama.client.JOllamaClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

class TestEmbeddingsResponse {

    static List<Double> embeddings = List.of(-0.3214254081249237, 0.5772175788879395, -2.817908525466919,
            -1.269736647605896, 0.8278807997703552, 1.5423403978347778, -0.2099379301071167, -0.23452191054821014,
            1.4592816829681396);
    static MockWebServer server;
    static JOllamaClient client;

    @BeforeAll
    static void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        String url = server.url("/api/embeddings").toString();
        client = JOllamaClient.builder(url).build();
    }

    @Test
    void embeddingsResponse() throws IOException {
        MockResponse mockResponse = new MockResponse();
        String json = Files.readString(Path.of("src/test/resources/responses/embeddings/embeddings.json"));
        mockResponse.setBody(json);
        server.enqueue(mockResponse);

        EmbeddingsResponse embeddingsResponse = client
                .embeddings(Model.NOMIC_EMBED_TEXT_LATEST, "What is the capital of Australia").get();
        assertAll(
                () -> assertEquals(768, embeddingsResponse.embedding().size()),
                () -> assertEquals(embeddings, embeddings.subList(0, 9)));

    }

    @AfterAll
    static void tearDown() throws IOException {
        server.shutdown();
    }

}
