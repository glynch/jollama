package com.glynch.ollama.prompt;

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

}
