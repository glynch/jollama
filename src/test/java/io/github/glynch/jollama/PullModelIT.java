package io.github.glynch.jollama;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.glynch.jollama.pull.PullResponse;

class PullModelIT extends AbstractJOllamaIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(PullModelIT.class);
    static final String IMAGE_NAME = "grahamlynch/jollama-phi3:0.1.42";

    PullModelIT() {
        super(IMAGE_NAME, LOGGER);
    }

    @Test
    @Order(9)
    void pullModel() {
        PullResponse response = client.pull(Model.NOMIC_EMBED_TEXT_LATEST).batch();
        assertAll(
                () -> assertEquals("success", response.status()),
                () -> assertEquals(2, client.list().models().size()));
    }

}
