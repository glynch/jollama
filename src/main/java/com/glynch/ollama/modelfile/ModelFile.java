package com.glynch.ollama.modelfile;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.glynch.ollama.Options;
import com.glynch.ollama.chat.Message;

public record ModelFile(String from, String template, Map<Options.Key, String> parameters, String system,
        String adapter,
        String license, List<Message> messages) {

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FROM ").append(from).append("\n");
        if (template != null) {
            builder.append("TEMPLATE ").append(template).append("\n");
        }
        if (parameters != null) {
            parameters.forEach((k, v) -> builder.append("PARAMETER ").append(k).append(" ").append(v).append("\n"));
        }
        if (system != null) {
            builder.append("SYSTEM ").append(system).append("\n");
        }
        if (adapter != null) {
            builder.append("ADAPTER ").append(adapter).append("\n");
        }
        if (license != null) {
            builder.append("LICENSE ").append(license).append("\n");
        }
        if (messages != null) {
            messages.forEach(
                    m -> builder.append("MESSAGE ").append(m.role()).append(" ").append(m.content()).append("\n"));
        }
        return builder.toString();
    }

    public static ModelFile of(String modelfile) {
        return null;
    }

    public static ModelFile of(Path path) {
        return null;
    }

    public ModelFile modelfile() {
        return null;
    }

    public static Builder from(String from) {
        return new DefaultModelFileBuilder(from);
    }

    public interface Builder {

        Builder template(String template);

        Builder parameters(Map<Options.Key, String> parameters);

        Builder parameter(Options.Key name, String value);

        Builder system(String system);

        Builder adapter(String adapter);

        Builder license(String license);

        Builder messages(List<Message> messages);

        Builder message(Message message);

        ModelFile build();
    }

    private static final class DefaultModelFileBuilder implements Builder {

        private final String from;
        private String template;
        private Map<Options.Key, String> parameters = new HashMap<>();
        private String system;
        private String adapter;
        private String license;
        private List<Message> messages = new ArrayList<>();

        DefaultModelFileBuilder(String from) {
            this.from = from;
        }

        @Override
        public Builder template(String template) {
            Objects.requireNonNull(template, "template");
            this.template = template;
            return this;
        }

        @Override
        public Builder parameters(Map<Options.Key, String> parameters) {
            Objects.requireNonNull(parameters, "parameters");
            this.parameters.putAll(parameters);
            return this;
        }

        @Override
        public Builder parameter(Options.Key key, String value) {
            Objects.requireNonNull(key, "key");
            Objects.requireNonNull(value, "value");
            this.parameters.put(key, value);
            return this;
        }

        @Override
        public Builder system(String system) {
            Objects.requireNonNull(system, "system");
            this.system = system;
            return this;
        }

        @Override
        public Builder adapter(String adapter) {
            Objects.requireNonNull(adapter, "adapter");
            this.adapter = adapter;
            return this;
        }

        @Override
        public Builder license(String license) {
            Objects.requireNonNull(license, "license");
            this.license = license;
            return this;
        }

        @Override
        public Builder messages(List<Message> messages) {
            Objects.requireNonNull(messages, "messages");
            this.messages.addAll(messages);
            return this;
        }

        @Override
        public Builder message(Message message) {
            Objects.requireNonNull(message, "message");
            this.messages.add(message);
            return this;
        }

        @Override
        public ModelFile build() {
            return new ModelFile(from, template, parameters, system, adapter, license, messages);
        }

    }

}
