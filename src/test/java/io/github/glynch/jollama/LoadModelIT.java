package io.github.glynch.jollama;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.glynch.jollama.client.JOllamaClientResponseException;
import io.github.glynch.jollama.process.ProcessModel;

class LoadModelIT extends AbstractJOllamaIT {

        static final Logger LOGGER = LoggerFactory.getLogger(LoadModelIT.class);
        static final String IMAGE_NAME = "grahamlynch/jollama-phi3:0.1.42";
        static final Model MODEL = Model.PHI_3_MINI;
        static final String MODEL_NAME = MODEL.toString();

        LoadModelIT() {
                super(IMAGE_NAME, LOGGER);
        }

        @Test
        void loadModel() {
                Optional<ProcessModel> ps = client.load(MODEL,
                                KeepAlive.DEFAULT);
                assertAll(
                                () -> assertEquals(true, ps.isPresent()),
                                () -> assertEquals(MODEL_NAME, ps.get().model()),
                                () -> assertEquals(MODEL.getName().toString(),
                                                ps.get().details().family()),
                                () -> assertEquals("3.8B", ps.get().details().parameterSize()),
                                () -> assertEquals("Q4_K_M", ps.get().details().quantizationLevel()),
                                () -> assertEquals("gguf", ps.get().details().format()),
                                () -> assertEquals(List.of("phi3"), ps.get().details().families()),
                                () -> assertEquals(MODEL.toString(), ps.get().name()));

        }

        @Test
        public void loadModelNotExists() {
                JOllamaClientResponseException e = assertThrows(JOllamaClientResponseException.class, () -> {
                        client.load("dummy", KeepAlive.DEFAULT);
                });

                assertEquals(404, e.getStatusCode());

        }

}
