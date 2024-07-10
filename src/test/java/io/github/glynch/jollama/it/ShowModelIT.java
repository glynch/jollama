package io.github.glynch.jollama.it;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.glynch.jollama.Model;
import io.github.glynch.jollama.show.ShowResponse;

class ShowModelIT extends AbstractJOllamaIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShowModelIT.class);
    private static final String IMAGE_NAME = "grahamlynch/jollama-phi3:0.1.48";

    ShowModelIT() {
        super(IMAGE_NAME, LOGGER);
    }

    @Test
    void showModel() {
        ShowResponse showResponse = client.show(Model.PHI_3_MINI, true);
        assertAll(
                () -> assertTrue(showResponse.license().contains("MIT License")),
                () -> assertTrue(!showResponse.parameters().isEmpty()),
                () -> assertTrue(showResponse.modelInfo().tokenizerGgmlTokens().size() > 0));

    }

}
