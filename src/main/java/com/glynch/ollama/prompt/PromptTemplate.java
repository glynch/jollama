package com.glynch.ollama.prompt;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface PromptTemplate {

    String template();

    String format(Object... args);

    String format(Map<String, Object> args);

    List<String> variables();

    public static PromptTemplate template(String template) {
        Objects.requireNonNull(template, "template cannot be null");
        return new StringPromptTemplate(template);
    }

    public static PromptTemplate template(Path path) throws UncheckedIOException {
        Objects.requireNonNull(path, "path cannot be null");
        String template = null;
        try {
            template = Files.readString(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return new StringPromptTemplate(template);
    }

}
