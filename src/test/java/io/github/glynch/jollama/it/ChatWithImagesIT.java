package io.github.glynch.jollama.it;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.glynch.jollama.Options;
import io.github.glynch.jollama.support.Image;

public class ChatWithImagesIT extends AbstractJOllamaIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatWithImagesIT.class);
    static final String IMAGE_NAME = "grahamlynch/jollama-llava:0.1.48";

    ChatWithImagesIT() {
        super(IMAGE_NAME, LOGGER);
    }

    @Test
    void chatWithImages() {

        Options options = Options.builder().temperature(0f).seed(42).build();
        String content = client
                .chat("llava", "Describe this image?",
                        Image.encode(Path.of("src/test/resources/pig.png")))
                .options(options)
                .batch()
                .message().content();

        assertTrue(content.contains("The image is a digital illustration featuring an animated character"));
    }

}
