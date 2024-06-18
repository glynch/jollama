package com.glynch.jollama;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glynch.jollama.modelfile.ModelFile;

class ShowModelIT extends AbstractJOllamaIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShowModelIT.class);
    private static final String IMAGE_NAME = "grahamlynch/jollama-phi3:0.1.42";

    ShowModelIT() {
        super(IMAGE_NAME, LOGGER);
    }

    @Test
    void showModel() {
        ModelFile modelFile = client.show(Model.PHI_3_MINI);
        assertAll(
                () -> assertTrue(modelFile.license().contains("MIT License")),
                () -> assertEquals(1, modelFile.parameters().size()));

    }

}
