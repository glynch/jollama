package io.github.glynch.jollama;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.glynch.jollama.create.CreateResponse;

class CreateModelIT extends AbstractJOllamaIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateModelIT.class);
    static final String IMAGE_NAME = "grahamlynch/jollama-phi3:0.1.42";

    public CreateModelIT() {
        super(IMAGE_NAME, LOGGER);
    }

    @Test
    void createModel() {
        CreateResponse response = client
                .create("mario-test",
                        "FROM phi3\nSYSTEM You are mario from Super Mario Bros.\nPARAMETER temperature 0\nPARAMETER seed 42")
                .batch();

        assertAll(
                () -> assertEquals("success", response.status()));

    }

}
