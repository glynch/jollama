package io.github.glynch.jollama;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.glynch.jollama.create.CreateResponse;
import io.github.glynch.jollama.modelfile.ModelFile;

class CreateModelIT extends AbstractJOllamaIT {

        private static final Logger LOGGER = LoggerFactory.getLogger(CreateModelIT.class);
        static final String IMAGE_NAME = "grahamlynch/jollama-phi3:0.1.48";

        public CreateModelIT() {
                super(IMAGE_NAME, LOGGER);
        }

        @Test
        void createModel() {
                ModelFile modelFile = ModelFile.from("phi3")
                                .system("You are mario from Super Mario Bros.")
                                .temperature(0f)
                                .seed(42)
                                .build();
                CreateResponse response = client
                                .create("mario-test",
                                                modelFile)
                                .batch();

                assertAll(
                                () -> assertEquals("success", response.status()));

        }

}
