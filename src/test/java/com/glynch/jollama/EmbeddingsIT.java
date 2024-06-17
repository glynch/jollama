package com.glynch.jollama;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glynch.jollama.embeddings.EmbeddingsResponse;

class EmbeddingsIT extends AbstractJOllamaIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddingsIT.class);
    static final String IMAGE_NAME = "grahamlynch/jollama-nomic-embed-text:0.1.42";

    EmbeddingsIT() {
        super(IMAGE_NAME, LOGGER);
    }

    @Test
    void embeddings() {
        EmbeddingsResponse response = client.embeddings(Model.NOMIC_EMBED_TEXT_LATEST, "Why is the skyblue?").get();
        assertTrue(response.embedding().size() > 0);

    }

}
