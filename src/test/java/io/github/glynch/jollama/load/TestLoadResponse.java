package io.github.glynch.jollama.load;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
import io.github.glynch.jollama.process.ProcessModel;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

class TestLoadResponse {

    static MockWebServer server;
    static JOllamaClient client;

    @BeforeAll
    static void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        server.setDispatcher(new LoadDispatcher());
        client = JOllamaClient.builder("http://localhost:" + server.getPort()).build();
    }

    @Test
    void loadResponse() {
        Optional<ProcessModel> response = client.load(Model.PHI_3_LATEST);
        if (response.isPresent()) {
            ProcessModel ps = response.get();
            assertAll(
                    () -> assertEquals(Model.PHI_3_LATEST.toString(), ps.model()),
                    () -> assertEquals(3912400384L, ps.size()),
                    () -> assertEquals("64c1188f2485448235b2d371639a127fc0e4dc2cd3c041152368883c42eb2bd1",
                            ps.digest()),
                    () -> assertEquals("",
                            ps.details().parentModel()),
                    () -> assertEquals("gguf", ps.details().format()),
                    () -> assertEquals("phi3", ps.details().family()),
                    () -> assertEquals(List.of("phi3"), ps.details().families()),
                    () -> assertEquals("3.8B", ps.details().parameterSize()),
                    () -> assertEquals("Q4_K_M", ps.details().quantizationLevel()),
                    () -> assertEquals(3912400384L, ps.sizeVram()));
        } else {
            fail("Failed in loadResponse");
        }

    }

    @AfterAll
    static void tearDown() throws IOException {
        server.shutdown();
    }

    private static class LoadDispatcher extends Dispatcher {

        @Override
        public MockResponse dispatch(RecordedRequest request) throws InterruptedException {

            try {
                switch (request.getPath()) {
                    case "/api/generate":
                        String generate = Files.readString(Path.of("src/test/resources/responses/load/generate.json"));
                        return new MockResponse().setResponseCode(200).setBody(generate);
                    case "/api/ps":

                        String ps = Files.readString(Path.of("src/test/resources/responses/load/ps.json"));
                        return new MockResponse().setResponseCode(200).setBody(ps);

                }
            } catch (Exception e) {

            }
            return new MockResponse().setResponseCode(404);
        }

    }

}
