package com.glynch.jollama;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DeleteModelIT extends AbstractJOllamaIT {

    DeleteModelIT() {
        super(IMAGE_NAME, LOGGER);
    }

    private static Logger LOGGER = LoggerFactory.getLogger(DeleteModelIT.class);
    private static final String IMAGE_NAME = "grahamlynch/jollama-phi3:0.1.42";

    @Test
    void deleteModel() {
        int status = client.delete(Model.PHI_3_MINI);

        assertAll(
                () -> assertEquals(200, status),
                () -> assertEquals(0, client.list().models().size()));
    }

}
