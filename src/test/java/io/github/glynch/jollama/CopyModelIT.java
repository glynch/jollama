package io.github.glynch.jollama;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CopyModelIT extends AbstractJOllamaIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(CopyModelIT.class);
    static final String IMAGE_NAME = "grahamlynch/jollama-phi3:0.1.42";

    CopyModelIT() {
        super(IMAGE_NAME, LOGGER);
    }

    @Test
    void copyModel() {
        int status = client.copy("phi3:mini", "phi3-copy");
        assertAll(
                () -> assertEquals(200, status),
                () -> assertEquals(2, client.list().models().size()));

    }

}
