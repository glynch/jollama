package io.github.glynch.jollama.list;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.glynch.jollama.Model;
import io.github.glynch.jollama.client.JOllamaClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

class TestListResponse {

    static MockWebServer server;
    static JOllamaClient client;

    @BeforeAll
    static void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        String url = server.url("/api/tags").toString();
        client = JOllamaClient.create(url);
    }

    @Test
    void listResponse() throws IOException {
        MockResponse mockResponse = new MockResponse();
        String json = Files.readString(Path.of("src/test/resources/responses/list/list.json"));
        mockResponse.setBody(json);
        server.enqueue(mockResponse);

        Optional<ListModel> response = client.list(Model.PHI_3_LATEST);
        if (response.isPresent()) {
            ListModel listModel = response.get();
            assertAll(
                    () -> assertEquals(Model.PHI_3_LATEST.toString(), listModel.model()),
                    () -> assertEquals(2393232963L, listModel.size()),
                    () -> assertEquals("64c1188f2485448235b2d371639a127fc0e4dc2cd3c041152368883c42eb2bd1",
                            listModel.digest()),
                    () -> assertEquals("gguf", listModel.details().format()),
                    () -> assertEquals("phi3", listModel.details().family()),
                    () -> assertEquals(List.of("phi3"), listModel.details().families()),
                    () -> assertEquals("3.8B", listModel.details().parameterSize()),
                    () -> assertEquals("Q4_K_M", listModel.details().quantizationLevel()));
        } else {
            fail("Failed in listResponse");
        }

    }

    @AfterAll
    static void tearDown() throws IOException {
        server.shutdown();
    }

}
