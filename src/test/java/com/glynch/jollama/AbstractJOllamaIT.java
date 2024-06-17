package com.glynch.jollama;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.ollama.OllamaContainer;
import org.testcontainers.utility.DockerImageName;

import com.glynch.jollama.client.JOllamaClient;

@TestInstance(Lifecycle.PER_CLASS)
abstract class AbstractJOllamaIT {

    private final OllamaContainer ollama;
    protected JOllamaClient client;

    public AbstractJOllamaIT(String model, Logger logger) {
        this.ollama = new OllamaContainer(DockerImageName.parse(model).asCompatibleSubstituteFor("ollama/ollama"));
        this.ollama.withLogConsumer(new Slf4jLogConsumer(logger));
    }

    @BeforeAll
    void beforeAll() {
        ollama.start();
        client = JOllamaClient.create(ollama.getEndpoint());
    }

    @AfterAll
    void afterAll() {
        ollama.stop();
    }
}
