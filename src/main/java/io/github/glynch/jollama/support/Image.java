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

    /**
     * Encode an image to a base64 string.
     * 
     * @param image The image.
     * @return The base64 string.
     */
    public static String encode(byte[] image) {
        return new String(Base64.getEncoder().encodeToString(image));
    }

    /**
     * Encode an image to a base64 string.
     * 
     * @param path The path to the image.
     * @return The base64 string.
     */
    public static String encode(Path path) {
        try {
            return encode(Files.readAllBytes(path));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Decode a base64 string to an image.
     * 
     * @param image The base64 string.
     * @return The image.
     */
    public static byte[] decode(String image) {
        return Base64.getDecoder().decode(image);
    }

}
