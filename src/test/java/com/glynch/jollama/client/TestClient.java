package com.glynch.jollama.client;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.ollama.OllamaContainer;
import org.testcontainers.utility.DockerImageName;

import com.glynch.jollama.KeepAlive;
import com.glynch.jollama.Model;
import com.glynch.jollama.create.CreateResponse;
import com.glynch.jollama.embeddings.EmbeddingsResponse;
import com.glynch.jollama.modelfile.ModelFile;
import com.glynch.jollama.process.ProcessModel;
import com.glynch.jollama.pull.PullResponse;

@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class TestClient {
    static final Logger LOGGER = LoggerFactory.getLogger(TestClient.class);
    static OllamaContainer ollama = new OllamaContainer(
            DockerImageName.parse("jollama-phi3:0.1.42").asCompatibleSubstituteFor("ollama/ollama"));
    static {
        ollama.withLogConsumer(new Slf4jLogConsumer(LOGGER));
    }
    static private JOllamaClient client;

    @BeforeAll
    static void beforeAll() throws UnsupportedOperationException, IOException, InterruptedException {
        ollama.start();
        client = JOllamaClient.create(ollama.getEndpoint());
    }

    @AfterAll
    static void afterAll() {
        ollama.stop();
    }

    @BeforeEach
    void beforeEach() {

    }

    @Test
    @Order(1)
    public void loadModel_Loaded() throws UnsupportedOperationException, IOException, InterruptedException {
        Optional<ProcessModel> ps = client.load(Model.PHI_3_LATEST, KeepAlive.DEFAULT);
        assertAll(
                () -> assertEquals(true, ps.isPresent()),
                () -> assertEquals(Model.PHI_3_LATEST.toString(), ps.get().model()),
                () -> assertEquals(Model.PHI_3_LATEST.getName().toString(), ps.get().details().family()),
                () -> assertEquals("3.8B", ps.get().details().parameterSize()),
                () -> assertEquals("Q4_K_M", ps.get().details().quantizationLevel()),
                () -> assertEquals("gguf", ps.get().details().format()),
                () -> assertEquals(List.of("phi3"), ps.get().details().families()),
                () -> assertEquals(Model.PHI_3_LATEST.toString(), ps.get().name()));

    }

    @Test
    @Order(2)
    public void loadModel_NotLoaded() {
        JOllamaClientResponseException e = assertThrows(JOllamaClientResponseException.class, () -> {
            client.load("dummy", KeepAlive.DEFAULT);
        });

        assertEquals(404, e.getStatusCode());

    }

    @Test
    @Order(3)
    public void createModel() {
        CreateResponse response = client
                .create("mario-test",
                        "FROM phi3\nSYSTEM You are mario from Super Mario Bros.\nPARAMETER temperature 0\nPARAMETER seed 42")
                .batch();

        System.out.println(client.list().models());

        assertEquals("success", response.status());

    }

    @Test
    @Order(4)
    public void blobExists() {
        int status = client.blobs("sha256:b26e6713dc749dda35872713fa19a568040f475cc71cb132cff332fe7e216462").exists();
        assertEquals(200, status);
    }

    @Test
    @Order(5)
    public void listModels() {
        assertEquals(2, client.list().models().size());
    }

    @Test
    @Order(6)
    public void showModel() {
        ModelFile modelFile = client.show("mario-test:latest");
        System.out.println(modelFile);
        assertAll(
                () -> assertEquals("You are mario from Super Mario Bros.",
                        modelFile.system()),
                () -> assertEquals(3, modelFile.parameters().size()),
                () -> assertEquals("0", modelFile.parameters().get("temperature")),
                () -> assertEquals("42", modelFile.parameters().get("seed")));

    }

    @Test
    @Order(7)
    public void deleteModel() {
        int status = client.delete("mario-test");
        assertAll(
                () -> assertEquals(200, status),
                () -> assertEquals(1, client.list().models().size()));

    }

    @Test
    @Order(8)
    public void copyModel() {
        int status = client.copy("phi3:latest", "mario-test");
        assertAll(
                () -> assertEquals(200, status),
                () -> assertEquals(2, client.list().models().size()));

    }

    @Test
    @Order(9)
    public void pullModel() {
        PullResponse response = client.pull(Model.NOMIC_EMBED_TEXT_LATEST).batch();
        assertAll(
                () -> assertEquals("success", response.status()),
                () -> assertEquals(3, client.list().models().size()));
    }

    @Test
    @Order(10)
    public void embeddings() {
        EmbeddingsResponse response = client.embeddings(Model.NOMIC_EMBED_TEXT_LATEST, "Why is the sky blue?").get();

        assertTrue(response.embedding().size() > 0);

    }

}
