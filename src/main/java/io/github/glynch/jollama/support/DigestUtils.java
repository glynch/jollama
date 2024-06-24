package io.github.glynch.jollama.support;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * Sha256 digest utilities.
 */
public class DigestUtils {

    public static final String SHA256_PREFIX = "sha256:";

    private DigestUtils() {
    }

    /**
     * Get the sha256 digest of the input.
     * 
     * @param input The input.
     * @return The digest.
     */
    public static byte[] sha256(byte[] input) {
        Objects.requireNonNull(input, "input cannot be null");
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the sha256 digest of the input.
     * 
     * @param input The input.
     * @return The digest.
     */
    public static byte[] sha256(String input) {
        Objects.requireNonNull(input, "input cannot be null");
        return sha256(input.getBytes());
    }

    /**
     * Get the sha256 digest of the input in hex.
     * 
     * The output is prefixed with "sha256:" and can be used as a digest.
     * 
     * @param input The input.
     * @return The digest in hex
     */
    public static String sha256hex(byte[] input) {
        return SHA256_PREFIX + bytesToHex(sha256(input));
    }

    /**
     * Get the sha256 digest of the input in hex.
     * 
     * The output is prefixed with "sha256:" and can be used as a digest in
     * 
     * @param input The input.
     * @return The digest in hex
     */
    public static String sha256hex(String input) {
        return SHA256_PREFIX + bytesToHex(sha256(input));
    }

    /**
     * Get the sha256 digest of the file at the path.
     * 
     * @param path The path to the file.
     * @return The digest.
     */
    public static byte[] sha256(Path path) {
        Objects.requireNonNull(path, "path cannot be null");
        MessageDigest digest = null;
        byte[] buffer = new byte[8192];
        try {
            digest = MessageDigest.getInstance("SHA-256");
            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(
                    Files.newInputStream(path, StandardOpenOption.READ))) {
                int size = -1;
                while ((size = bufferedInputStream.read(buffer)) != -1) {
                    digest.update(buffer, 0, size);
                }
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (NoSuchAlgorithmException e) {

        }
        return digest.digest();
    }

    /**
     * Get the sha256 digest of the file at the path in hex.
     * 
     * The output is prefixed with "sha256:" and can be used as a digest in
     * 
     * @param path The path to the file.
     * @return The digest in hex.
     */
    public static String sha256hex(Path path) {
        return SHA256_PREFIX + bytesToHex(sha256(path));
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
