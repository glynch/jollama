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
    void modelFileValidPath() {
        ModelFile modelFile = ModelFile.parse(Path.of("src/test/resources/test.modelfile"));
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
    void modelFileInValidPath() {
        assertThrows(UncheckedIOException.class,
                () -> ModelFile.parse(Path.of("src/test/resources/invalid-path.modelfile")));
    }

    @Test
    void modeFileInvalidModelFileValidPath() {

        assertThrows(
                InvalidModelFileException.class,
                () -> ModelFile.parse(Path.of("src/test/resources/invalid.modelfile")));
    }

    @Test
    void modelFileBuilder() {
        Message user = Message.user("Why is the sky blue?");
        Message assistant = Message.assistant("The sky is blue because of Rayleigh scattering.");
        Message system = Message.system("You are meteorological expert.");
        ModelFile modelFile = builder.system("You are mario from Super Mario Bros.")
                .license(LICENSE_STRING)
                .message(system)
                .message(user)
                .message(assistant)
                .stop("<|end|>")
                .stop("<|user|>")
                .stop("<|assistant|>")
                .temperature(0f)
                .seed(42)
                .build();
        assertAll(
                () -> assertEquals("llama3", modelFile.from()),
                () -> assertEquals("You are mario from Super Mario Bros.", modelFile.system()),
                () -> assertEquals(LICENSE_STRING, modelFile.license()),
                () -> assertEquals(system, modelFile.messages().get(0)),
                () -> assertEquals(user, modelFile.messages().get(1)),
                () -> assertEquals(assistant, modelFile.messages().get(2)),
                () -> assertEquals(3, modelFile.parameters().size()),
                () -> assertEquals(0f, modelFile.temperature()),
                () -> assertEquals(42, modelFile.seed()),
                () -> assertEquals(3, modelFile.stop().size()));
    }

}
