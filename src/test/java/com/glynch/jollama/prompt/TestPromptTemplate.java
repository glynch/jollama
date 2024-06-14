package com.glynch.jollama.prompt;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class TestPromptTemplate {

    @Test
    public void testValidPromptTemplateFromStringOneVariable() {
        PromptTemplate template = PromptTemplate.template("Hello, {name}!");
        assertEquals("Hello, Graham!", template.format("Graham"));
    }

    @Test
    public void testPromptTemplateTemplate() {
        PromptTemplate template = PromptTemplate.template("Hello, {name} from {country}!");
        assertAll(
                () -> assertEquals("Hello, {name} from {country}!", template.template()),
                () -> assertEquals(2, template.variables().size()));
    }

    @Test
    public void testValidPromptTemplateFromStringTwoVariables() {
        PromptTemplate template = PromptTemplate.template("Hello, {name} from {country}!");
        assertEquals("Hello, Graham from Australia!", template.format("Graham", "Australia"));
    }

    @Test
    public void testValidPromptTemplateFromStringOneVariableMap() {
        Map<String, Object> arguments = Map.of("name", "Graham");
        PromptTemplate template = PromptTemplate.template("Hello, {name}!");
        assertEquals("Hello, Graham!", template.format(arguments));
    }

    @Test
    public void testValidPromptTemplateFromStringTwoVariablesMap() {
        Map<String, Object> arguments = Map.of("country", "Australia", "name", "Graham");
        PromptTemplate template = PromptTemplate.template("Hello, {name} from {country}!");
        assertEquals("Hello, Graham from Australia!", template.format(arguments));
    }

    @Test
    public void testValidPromptTemplateFromStringVariables() {
        List<String> expected = List.of("name", "country");
        PromptTemplate template = PromptTemplate.template("Hello, {name} from {country}!");
        assertEquals(expected, template.variables());
    }

    @Test
    public void testPromptTemplateFromStringNoVariables() {
        try {
            PromptTemplate.template("Hello, world!");
        } catch (IllegalArgumentException e) {
            assertEquals("No variables found in template", e.getMessage());
        }
    }

    @Test
    public void testPromptTemplateFromStringExtraArguments() {
        try {
            Map<String, Object> arguments = Map.of("country", "Australia", "name", "Graham");
            PromptTemplate template = PromptTemplate.template("Hello, {name}!");
            template.format(arguments);
        } catch (IllegalArgumentException e) {
            assertEquals("Incorrect number of arguments. Expected 1 but got 2", e.getMessage());
        }
    }

    @Test
    public void testPromptTemplateFromStringExtraArgumentsMap() {
        try {
            PromptTemplate template = PromptTemplate.template("Hello, {name}!");
            template.format("Graham", "Australia");
        } catch (IllegalArgumentException e) {
            assertEquals("Incorrect number of arguments. Expected 1 but got 2", e.getMessage());
        }
    }

    @Test
    public void testPromptTemplateFromValidPath() {
        PromptTemplate template = PromptTemplate.template(Path.of("src/test/resources/prompts/prompt-template.txt"));
        assertEquals("Hello, Graham\n from Australia!", template.format("Graham", "Australia"));
    }

    @Test
    public void testPromptTemplateFromInvalidPath() {
        assertThrows(UncheckedIOException.class, () -> {
            PromptTemplate.template(Path.of("src/test/resources/invalid-prompt-template.txt"));
        });
    }

}
