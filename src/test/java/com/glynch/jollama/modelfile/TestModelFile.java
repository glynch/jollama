package com.glynch.jollama.modelfile;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.glynch.jollama.chat.Message;

public class TestModelFile {

    private static final String LICENSE_STRING = "META LLAMA 3 COMMUNITY LICENSE AGREEMENT\n\nMeta Llama 3 Version Release Date: April 18, 2024\n“Agreement” means the terms and conditions for use, reproduction, distribution and modification of the Llama Materials set forth herein.";

    ModelFile.Builder builder;

    @BeforeEach
    public void init() {
        builder = ModelFile.from("llama3");
    }

    @Test
    public void testModelFileValidFrom() {
        ModelFile modelFile = builder.build();
        assertEquals("llama3", modelFile.from());
    }

    @Test
    public void testModelFileValidPath() {
        ModelFile modelFile = ModelFile.parse(Path.of("src/test/resources/test.modelfile"));
        System.out.println(modelFile.toString());
        Message user = Message.user("This is a user message.", List.of());
        Message assistant = Message.assistant("This is the assistant answer.", List.of());
        assertAll(
                () -> assertEquals("llama3", modelFile.from()),
                () -> assertEquals("You are mario from Super MarioBros.", modelFile.system()),
                () -> assertEquals(LICENSE_STRING, modelFile.license()),
                () -> assertEquals(user, modelFile.messages().get(0)),
                () -> assertEquals(assistant, modelFile.messages().get(1)));
    }

    @Test
    public void testModelFileInValidPath() {
        assertThrows(UncheckedIOException.class, () -> {
            ModelFile.parse(Path.of("src/test/resources/invalid-path.modelfile"));
        });
    }

    @Test
    public void testInvalidModelFileValidPath() {

        assertThrows(
                InvalidModelFileException.class,
                () -> {
                    ModelFile.parse(Path.of("src/test/resources/invalid.modelfile"));
                });
    }

    // @Test
    // public void modeFileToString() throws IOException {
    // ModelFile modelFile =
    // ModelFile.parse(Path.of("src/test/resources/test.modelfile"));
    // String expected =
    // Files.readString(Path.of("src/test/resources/test.modelfile"));
    // assertEquals(expected, modelFile.toString());
    // }

}
