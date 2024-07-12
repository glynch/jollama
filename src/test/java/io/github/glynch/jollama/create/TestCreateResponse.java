package io.github.glynch.jollama.create;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.glynch.jollama.client.JOllamaClient;
import io.github.glynch.jollama.modelfile.ModelFile;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class TestCreateResponse {

    static MockWebServer server;
    static JOllamaClient client;

    @BeforeAll
    static void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        String url = server.url("/api/create").toString();
        client = JOllamaClient.builder(url).build();
    }

    @Test
    void createResponseBatch() throws IOException {
        MockResponse mockResponse = new MockResponse();
        String json = Files.readString(Path.of("src/test/resources/responses/create/batch.json"));
        mockResponse.setBody(json);
        server.enqueue(mockResponse);

        ModelFile modelFile = ModelFile.from("llama3").system("You are Mario from Mario Bros.").build();
        CreateResponse createResponse = client.create("mario-test", modelFile).batch();

        assertEquals("success", createResponse.status());
    }

    @Test
    void createResponseStream() throws IOException {
        MockResponse mockResponse = new MockResponse();
        String json = Files.readString(Path.of("src/test/resources/responses/create/stream.json"));
        mockResponse.setBody(json);
        server.enqueue(mockResponse);

        ModelFile modelFile = ModelFile.from("llama3").system("You are Mario from Mario Bros.").build();
        Flux<CreateResponse> response = client.create("mario-test", modelFile).stream();
        StepVerifier.create(response)
                .assertNext(
                        r -> assertEquals(
                                "using existing layer sha256:00e1317cbf74d901080d7100f57580ba8dd8de57203072dc6f668324ba545f29",
                                r.status()))
                .assertNext(
                        r -> assertEquals(
                                "using existing layer sha256:4fa551d4f938f68b8c1e6afa9d28befb70e3f33f75d0753248d530364aeea40f",
                                r.status()))
                .assertNext(
                        r -> assertEquals(
                                "using existing layer sha256:8ab4849b038cf0abc5b1c9b8ee1443dca6b93a045c2272180d985126eb40bf6f",
                                r.status()))
                .assertNext(
                        r -> assertEquals(
                                "creating new layer sha256:1741cf59ce26ff01ac614d31efc700e21e44dd96aed60a7c91ab3f47e440ef94",
                                r.status()))
                .assertNext(
                        r -> assertEquals(
                                "using existing layer sha256:c0aac7c7f00d8a81a8ef397cd78664957fbe0e09f87b08bc7afa8d627a8da87f",
                                r.status()))
                .assertNext(
                        r -> assertEquals(
                                "creating new layer sha256:6ae2b1aa7aa884a6f332ac8fc6731eddf939f5acb6dde3cf9b2ae75b9749f68a",
                                r.status()))
                .assertNext(
                        r -> assertEquals(
                                "writing manifest",
                                r.status()))
                .assertNext(
                        r -> assertEquals(
                                "success",
                                r.status()))
                .verifyComplete();

    }

    @AfterAll
    static void tearDown() throws IOException {
        server.shutdown();
    }

}
