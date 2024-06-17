package com.glynch.jollama.client;

import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
public class TestClient {
    // static final Logger LOGGER = LoggerFactory.getLogger(TestClient.class);
    // static OllamaContainer ollama = new OllamaContainer(
    // DockerImageName.parse("grahamlynch/jollama-phi3:latest").asCompatibleSubstituteFor("ollama/ollama"));
    // static {
    // ollama.withLogConsumer(new Slf4jLogConsumer(LOGGER));
    // }
    // static private JOllamaClient client;

    // @BeforeAll
    // static void beforeAll() {
    // ollama.start();
    // client = JOllamaClient.create(ollama.getEndpoint());
    // }

    // @AfterAll
    // static void afterAll() {
    // ollama.stop();
    // }

    // @BeforeEach
    // void beforeEach() {

    // }

    // @Test
    // @Order(1)
    // public void loadModel_Loaded() {
    // Optional<ProcessModel> ps = client.load(Model.PHI_3_LATEST,
    // KeepAlive.DEFAULT);
    // assertAll(
    // () -> assertEquals(true, ps.isPresent()),
    // () -> assertEquals(Model.PHI_3_LATEST.toString(), ps.get().model()),
    // () -> assertEquals(Model.PHI_3_LATEST.getName().toString(),
    // ps.get().details().family()),
    // () -> assertEquals("3.8B", ps.get().details().parameterSize()),
    // () -> assertEquals("Q4_K_M", ps.get().details().quantizationLevel()),
    // () -> assertEquals("gguf", ps.get().details().format()),
    // () -> assertEquals(List.of("phi3"), ps.get().details().families()),
    // () -> assertEquals(Model.PHI_3_LATEST.toString(), ps.get().name()));

    // }

    // @Test
    // @Order(2)
    // public void loadModel_NotLoaded() {
    // JOllamaClientResponseException e =
    // assertThrows(JOllamaClientResponseException.class, () -> {
    // client.load("dummy", KeepAlive.DEFAULT);
    // });

    // assertEquals(404, e.getStatusCode());

    // }

    // @Test
    // @Order(3)
    // public void createModel() {
    // CreateResponse response = client
    // .create("mario-test",
    // "FROM phi3\nSYSTEM You are mario from Super Mario Bros.\nPARAMETER
    // temperature 0\nPARAMETER seed 42")
    // .batch();

    // System.out.println(client.list().models());

    // assertEquals("success", response.status());

    // }

    // @Test
    // @Order(5)
    // public void listModels() {
    // assertEquals(2, client.list().models().size());
    // }

    // @Test
    // @Order(10)
    // public void embeddings() {
    // EmbeddingsResponse response =
    // client.embeddings(Model.NOMIC_EMBED_TEXT_LATEST, "Why is the sky
    // blue?").get();

    // assertTrue(response.embedding().size() > 0);

    // }

}
