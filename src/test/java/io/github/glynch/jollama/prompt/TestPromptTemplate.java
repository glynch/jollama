package io.github.glynch.jollama.prompt;

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
    void promptTemplateFromStringOneVariable() {
        PromptTemplate template = PromptTemplate.template("Hello, {name}!");
        assertEquals("Hello, Graham!", template.format("Graham"));
    }

    @Test
    void promptTemplateTemplate() {
        PromptTemplate template = PromptTemplate.template("Hello, {name} from {country}!");
        assertAll(
                () -> assertEquals("Hello, {name} from {country}!", template.template()),
                () -> assertEquals(2, template.variables().size()));
    }

    @Test
    void promptTemplateFromStringTwoVariablesVarargs() {
        PromptTemplate template = PromptTemplate.template("Hello, {name} from {country}!");
        assertEquals("Hello, Graham from Australia!", template.format("Graham", "Australia"));
    }

    @Test
    void promptTemplateFromStringOneVariableMap() {
        Map<String, Object> arguments = Map.of("name", "Graham");
        PromptTemplate template = PromptTemplate.template("Hello, {name}!");
        assertEquals("Hello, Graham!", template.format(arguments));
    }

    @Test
    void promptTemplateFromStringTwoVariablesMap() {
        Map<String, Object> arguments = Map.of("country", "Australia", "name", "Graham");
        PromptTemplate template = PromptTemplate.template("Hello, {name} from {country}!");
        assertEquals("Hello, Graham from Australia!", template.format(arguments));
    }

    @Test
    void promptTemplateFromStringWithVariables() {
        List<String> expected = List.of("name", "country");
        PromptTemplate template = PromptTemplate.template("Hello, {name} from {country}!");
        assertEquals(expected, template.variables());
    }

    @Test
    void promptTemplateFromStringWithNoVariables() {
        try {
            PromptTemplate.template("Hello, world!");
        } catch (IllegalArgumentException e) {
            assertEquals("No variables found in template", e.getMessage());
        }
    }

    @Test
    void promptTemplateFromStringExtraArgumentsVarargs() {
        try {
            Map<String, Object> arguments = Map.of("country", "Australia", "name", "Graham");
            PromptTemplate template = PromptTemplate.template("Hello, {name}!");
            template.format(arguments);
        } catch (IllegalArgumentException e) {
            assertEquals("Incorrect number of arguments. Expected 1 but got 2", e.getMessage());
        }
    }

    @Test
    void promptTemplateFromStringExtraArgumentsMap() {
        try {
            PromptTemplate template = PromptTemplate.template("Hello, {name}!");
            template.format("Graham", "Australia");
        } catch (IllegalArgumentException e) {
            assertEquals("Incorrect number of arguments. Expected 1 but got 2", e.getMessage());
        }
    }

    @Test
    void promptTemplateFromValidPath() {
        PromptTemplate template = PromptTemplate.template(Path.of("src/test/resources/prompts/prompt-template.txt"));
        assertEquals("Hello, Graham\n from Australia!", template.format("Graham", "Australia"));
    }

    @Test
    void promptTemplateFromInvalidPath() {
        assertThrows(UncheckedIOException.class,
                () -> PromptTemplate.template(Path.of("src/test/resources/invalid-prompt-template.txt")));
    }

    @Test
    void promtTemplateJSON() {
        PromptTemplate template = PromptTemplate
                .template(Path.of("src/test/resources/prompts/prompt-template-json.txt"));
        assertEquals(1, template.variables().size());
    }

}
