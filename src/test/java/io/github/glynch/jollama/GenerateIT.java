package io.github.glynch.jollama;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.glynch.jollama.generate.GenerateResponse;

class GenerateIT extends AbstractJOllamaIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateIT.class);
    static final String IMAGE_NAME = "grahamlynch/jollama-phi3:0.1.48";

    GenerateIT() {
        super(IMAGE_NAME, LOGGER);
    }

    @Test
    void generateBatch() {
        GenerateResponse response = client.generate(Model.PHI_3_MINI, "Why is the sky blue?").batch();
        assertAll(
                () -> assertTrue(response.done()),
                () -> assertTrue(!response.response().isEmpty()),
                () -> assertEquals("stop", response.doneReason()));

    }

    @Test
    void generateStream() {
        StringBuilder response = new StringBuilder();
        client.generate(Model.PHI_3_MINI, "Why is the sky blue?")
                .stream()
                .subscribe(r -> {
                    response.append(r.response());
                });
        assertAll(
                () -> assertTrue(!response.isEmpty()));

    }

}
