package io.github.glynch.jollama;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import io.github.glynch.jollama.support.Image;

class TestImage {

    @Test
    void encode() throws IOException {
        String actual = Image.encode(Path.of("src/test/resources/pig.png"));
        String expected = Files.readString(Path.of("src/test/resources/pig.base64"));
        assertEquals(expected, actual);
    }

    @Test
    void decode() throws IOException {
        byte[] actual = Image.decode(Files.readString(Path.of("src/test/resources/pig.base64")));
        byte[] expected = Files.readAllBytes(Path.of("src/test/resources/pig.png"));
        assertTrue(Arrays.equals(expected, actual));

    }

}
