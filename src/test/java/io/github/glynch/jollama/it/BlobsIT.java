package io.github.glynch.jollama.it;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BlobsIT extends AbstractJOllamaIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlobsIT.class);
    static final String IMAGE_NAME = "grahamlynch/jollama-phi3:0.1.48";

    BlobsIT() {
        super(IMAGE_NAME, LOGGER);
    }

    @Test
    void blobExists() {
        int status = client.blobs().exists("sha256:b26e6713dc749dda35872713fa19a568040f475cc71cb132cff332fe7e216462");
        assertEquals(200, status);
    }

}
