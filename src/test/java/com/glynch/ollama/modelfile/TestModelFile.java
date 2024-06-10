package com.glynch.ollama.modelfile;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.glynch.ollama.chat.Message;

public class TestModelFile {

    private static final String LICENSE_STRING = "META LLAMA 3 COMMUNITY LICENSE AGREEMENT\n\nMeta Llama 3 Version Release Date: April 18, 2024\n“Agreement” means the terms and conditions for use, reproduction, distribution and modification of the Llama Materials set forth herein.";

    ModelFile.Builder builder;

    @BeforeEach
    public void init() {
        builder = ModelFile.from("llama3");
    }

    @Test
    public void whenFromReturnsCorrectValue() {
        ModelFile modelFile = builder.build();
        assertEquals("llama3", modelFile.from());
    }

    @Test
    public void whenValidModelFileFromPath() {
        ModelFile modelFile = ModelFile.parse(Path.of("src/test/resources/test.modelfile"));
        assertEquals("llama3", modelFile.from());
        assertEquals("You are mario from Super MarioBros.", modelFile.system());
        assertEquals(LICENSE_STRING, modelFile.license());
        Message user = Message.user("This is a user message.", List.of());
        Message assistant = Message.assistant("This is the assistant answer.", List.of());
        assertEquals(user, modelFile.messages().get(0));
        assertEquals(assistant, modelFile.messages().get(1));
    }

}
