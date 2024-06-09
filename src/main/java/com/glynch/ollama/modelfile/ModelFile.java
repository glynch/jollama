package com.glynch.ollama.modelfile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonValue;
import com.glynch.ollama.chat.Message;
import com.glynch.ollama.chat.Role;

public record ModelFile(String from, String template, Map<Key, Object> parameters, String system,
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
            parameters.forEach((k, v) -> {
                if (k == Key.STOP) {
                    @SuppressWarnings("unchecked")
                    List<String> stops = (List<String>) v;
                    stops.forEach(stop -> builder.append("PARAMETER ").append(k).append(" ").append(stop).append("\n"));
                    return;
                }
                builder.append("PARAMETER ").append(k).append(" ").append(v).append("\n");
            });
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

    public static enum Key {

        SEED("seed"),
        NUM_PREDICT("num_predict"),
        TOP_K("top_k"),
        TOP_P("top_p"),
        TFS_Z("tfs_z"),
        REPEAT_LAST_N("repeat_last_n"),
        TEMPERATURE("temperature"),
        REPEAT_PENALTY("repeat_penalty"),
        MIROSTAT("mirostat"),
        MIROSTAT_TAU("mirostat_tau"),
        MIROSTAT_ETA("mirostat_eta"),
        STOP("stop"),
        NUM_CTX("num_ctx");

        private final String value;

        Key(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }

        public static Key of(String value) {
            return Arrays.stream(values()).filter(v -> v.getValue().equals(value)).findFirst()
                    .orElse(null);
        }
    }

    public static ModelFile parse(String modelfile) {
        return parse(modelfile.lines());
    }

    public static ModelFile parse(Path path) throws InvalidModelFileException, IOException {
        return parse(Files.readAllLines(path).stream());
    }

    private static ModelFile parse(Stream<String> lines) {
        String from = null;
        Map<Key, Object> parameters = new HashMap<>();
        String template = null;
        String system = null;
        String adapter = null;
        String license = null;
        List<Message> messages = new ArrayList<>();
        List<String> stop = new ArrayList<>();

        for (String line : lines.collect(Collectors.toList())) {
            String[] parts = line.split(" ", 2);
            if (parts.length < 2) {
                continue;
            }
            String key = parts[0];
            String value = parts[1];
            switch (key) {
                case "FROM":
                    from = value;
                    break;
                case "TEMPLATE":
                    template = value;
                    break;
                case "SYSTEM":
                    system = value;
                    break;
                case "ADAPTER":
                    adapter = value;
                    break;
                case "LICENSE":
                    license = value;
                    break;
                case "MESSAGE":
                    String[] messageParts = value.split(" ", 2);
                    if (messageParts.length == 2) {
                        Role role = Role.of(messageParts[0]);
                        messages.add(new Message(role, messageParts[1], List.of()));
                    }
                    break;
                case "PARAMETER":
                    String[] parameterParts = value.split(" ", 2);
                    if (parameterParts.length == 2) {
                        Key parameterKey = Key.of(parameterParts[0]);
                        if (parameterKey == Key.STOP) {
                            stop.add(parameterParts[1]);
                        } else {
                            Object parameterValue = parameterParts[1];
                            parameters.put(parameterKey, parameterValue);
                        }

                    }
                    break;
                default:
                    break;
            }
        }
        if (from == null) {
            throw new InvalidModelFileException("FROM not found");
        }
        parameters.put(Key.STOP, stop);
        return new ModelFile(from, template, parameters, system, adapter, license, messages);
    }

    public static Builder from(String from) {
        return new DefaultModelFileBuilder(from);
    }

    public interface Builder {

        Builder template(String template);

        Builder system(String system);

        Builder adapter(String adapter);

        Builder license(String license);

        Builder messages(List<Message> messages);

        Builder message(Message message);

        Builder seed(Integer seed);

        Builder mirostat(Float mirostat);

        Builder mirostatTau(Float mirostatTau);

        Builder mirostatEta(Float mirostatEta);

        Builder numCtx(Integer numCtx);

        Builder repeatLastN(Integer repeatLastN);

        Builder repeatPenalty(Float repeatPenalty);

        Builder temperature(Float temperature);

        Builder topK(Integer topK);

        Builder topP(Float topP);

        Builder tfsZ(Float tfsZ);

        Builder stop(String stop);

        ModelFile build();
    }

    private static final class DefaultModelFileBuilder implements Builder {

        private final String from;
        private String template;
        private Map<Key, Object> parameters = new HashMap<>();
        private String system;
        private String adapter;
        private String license;
        private List<Message> messages = new ArrayList<>();
        private List<String> stops = new ArrayList<>();

        DefaultModelFileBuilder(String from) {
            this.from = from;
        }

        @Override
        public Builder template(String template) {
            Objects.requireNonNull(template, "template cannot be null");
            this.template = template;
            return this;
        }

        @Override
        public Builder system(String system) {
            Objects.requireNonNull(system, "system cannot be null");
            this.system = system;
            return this;
        }

        @Override
        public Builder adapter(String adapter) {
            Objects.requireNonNull(adapter, "adapter cannot be null");
            this.adapter = adapter;
            return this;
        }

        @Override
        public Builder license(String license) {
            Objects.requireNonNull(license, "license cannot be null");
            this.license = license;
            return this;
        }

        @Override
        public Builder messages(List<Message> messages) {
            Objects.requireNonNull(messages, "messages cannot be null");
            this.messages.addAll(messages);
            return this;
        }

        @Override
        public Builder message(Message message) {
            Objects.requireNonNull(message, "message cannot be null");
            this.messages.add(message);
            return this;
        }

        @Override
        public Builder seed(Integer seed) {
            Objects.requireNonNull(seed, "seed cannot be null");
            parameters.put(Key.SEED, seed);
            return this;
        }

        @Override
        public Builder mirostat(Float mirostat) {
            Objects.requireNonNull(mirostat, "mirostat cannot be null");
            parameters.put(Key.MIROSTAT, mirostat);
            return this;
        }

        @Override
        public Builder mirostatTau(Float mirostatTau) {
            Objects.requireNonNull(mirostatTau, "mirostatTau cannot be null");
            parameters.put(Key.MIROSTAT_TAU, mirostatTau);
            return this;
        }

        @Override
        public Builder mirostatEta(Float mirostatEta) {
            Objects.requireNonNull(mirostatEta, "mirostatEta cannot be null");
            parameters.put(Key.MIROSTAT_ETA, mirostatEta);
            return this;
        }

        @Override
        public Builder numCtx(Integer numCtx) {
            Objects.requireNonNull(numCtx, "numCtx cannot be null");
            parameters.put(Key.NUM_CTX, numCtx);
            return this;
        }

        @Override
        public Builder repeatLastN(Integer repeatLastN) {
            Objects.requireNonNull(repeatLastN, "repeatLastN cannot be null");
            parameters.put(Key.REPEAT_LAST_N, repeatLastN);
            return this;
        }

        @Override
        public Builder repeatPenalty(Float repeatPenalty) {
            Objects.requireNonNull(repeatPenalty, "repeatPenalty cannot be null");
            parameters.put(Key.REPEAT_PENALTY, repeatPenalty);
            return this;
        }

        @Override
        public Builder temperature(Float temperature) {
            Objects.requireNonNull(temperature, "temperature cannot be null");
            parameters.put(Key.TEMPERATURE, temperature);
            return this;
        }

        @Override
        public Builder topK(Integer topK) {
            Objects.requireNonNull(topK, "topK cannot be null");
            parameters.put(Key.TOP_K, topK);
            return this;
        }

        @Override
        public Builder topP(Float topP) {
            Objects.requireNonNull(topP, "topP cannot be null");
            parameters.put(Key.TOP_P, topP);
            return this;
        }

        @Override
        public Builder tfsZ(Float tfsZ) {
            Objects.requireNonNull(tfsZ, "tfsZ cannot be null");
            parameters.put(Key.TFS_Z, tfsZ);
            return this;
        }

        @Override
        public Builder stop(String stop) {
            Objects.requireNonNull(stop, "stop cannot be null");
            stops.add(stop);
            parameters.put(Key.STOP, stops);
            return this;
        }

        @Override
        public ModelFile build() {
            return new ModelFile(from, template, parameters, system, adapter, license, messages);
        }

    }

}
