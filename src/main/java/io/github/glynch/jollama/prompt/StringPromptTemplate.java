package io.github.glynch.jollama.prompt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class StringPromptTemplate implements PromptTemplate {

    private static final Pattern VARIABLES_PATTERN = Pattern.compile("\\{(.*?)\\}", Pattern.DOTALL | Pattern.MULTILINE);
    private static final Pattern REPLACE_PATTERN = Pattern.compile("(\\{(.*?)\\})", Pattern.DOTALL | Pattern.MULTILINE);
    private final String template;
    private final List<String> variables = new ArrayList<>();

    StringPromptTemplate(String template) {
        this.template = template;
        var matcher = VARIABLES_PATTERN.matcher(template);
        while (matcher.find()) {
            variables.add(matcher.group(1));
        }
        if (variables.isEmpty()) {
            throw new IllegalArgumentException("No variables found in template");
        }

    }

    @Override
    public String format(Object... args) {
        Objects.requireNonNull(args, "args cannot be null");
        if (args.length != variables.size()) {
            throw new IllegalArgumentException(
                    "Incorrect number of arguments. Expected " + variables.size() + " but got "
                            + args.length);
        }
        var output = new StringBuilder();
        var matcher = REPLACE_PATTERN.matcher(template);
        int lastIndex = 0;
        while (matcher.find()) {
            var variable = matcher.group(2);
            var index = variables.indexOf(variable);
            output.append(template, lastIndex, matcher.start()).append(args[index]);

            lastIndex = matcher.end();

        }
        if (lastIndex < template.length()) {
            output.append(template, lastIndex, template.length());
        } else {
            // Do nothing
        }
        return output.toString();
    }

    @Override
    public String format(Map<String, Object> args) {
        Objects.requireNonNull(args, "args cannot be null");
        if (args.size() != variables.size()) {
            throw new IllegalArgumentException(
                    "Incorrect number of arguments. Expected " + variables.size() + " but got "
                            + args.size());
        }
        var output = new StringBuilder();
        var matcher = REPLACE_PATTERN.matcher(template);
        int lastIndex = 0;
        while (matcher.find()) {
            var variable = matcher.group(2);
            output.append(template, lastIndex, matcher.start()).append(args.get(variable));

            lastIndex = matcher.end();

        }
        if (lastIndex < template.length()) {
            output.append(template, lastIndex, template.length());
        } else {
            // Do nothing
        }
        return output.toString();
    }

    @Override
    public List<String> variables() {
        return Collections.unmodifiableList(variables);
    }

    @Override
    public String template() {
        return template;
    }

}
