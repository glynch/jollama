package io.github.glynch.jollama.support;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

/**
 * Image encoding and decoding.
 */
public class Image {

    private Image() {
    }

    public static String encode(byte[] image) {
        return new String(Base64.getEncoder().encodeToString(image));
    }

    public static String encode(Path path) {
        try {
            return encode(Files.readAllBytes(path));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static byte[] decode(String image) {
        return Base64.getDecoder().decode(image);
    }

}
