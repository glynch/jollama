package io.github.glynch.jollama.it;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.glynch.jollama.Model;

class ListModelsIT extends AbstractJOllamaIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListModelsIT.class);

    private static final String IMAGE_NAME = "grahamlynch/jollama-phi3:0.1.48";

    ListModelsIT() {
        super(IMAGE_NAME, LOGGER);
    }

    @Test
    void listModels() {
        assertEquals(1, client.list().models().size());
    }

    @Test
    public void listModelsByModel() {
        assertEquals(Model.PHI_3_MINI.toString(), client.list(Model.PHI_3_MINI).get().name());
    }

}
