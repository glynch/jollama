package io.github.glynch.jollama.support;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TestDigestUtils {

    static String SHA256 = "sha256:bad9ea434b83cd45ad33ad73060a69c6cb4e6add9c9dc75e69258779d0ed2eab";
    static String value = "What is the capital of Australia?";

    @Test
    void encode() {

        assertAll(
                () -> assertEquals(SHA256,
                        DigestUtils.sha256hex(value)),
                () -> assertEquals(SHA256, DigestUtils.sha256hex(value.getBytes())));

    }

    // @Test
    // void decode() {
    // assertEquals(value, SHA256);
    // }

}
