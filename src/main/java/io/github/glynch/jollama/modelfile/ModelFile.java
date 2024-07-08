package io.github.glynch.jollama.modelfile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonValue;

import io.github.glynch.jollama.ParameterKey;
import io.github.glynch.jollama.chat.Message;
import io.github.glynch.jollama.chat.Role;

/**
 * Model file.
 * 
 * @param from       The model name or path.
 * @param adapter    The adapter.
 * @param template   The template.
 * @param system     The system.
 * @param messages   The messages.
 * @param parameters The parameters.
 * @param license    The license.
 * 
 */
public record ModelFile(String from, String adapter, String template, String system, List<Message> messages,
        Map<ParameterKey, Object> parameters,
        String license) {

    private static final Pattern FROM_PATTERN = Pattern.compile("^FROM\\s+(.*?)\\s*$",
            Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);

    private static final Pattern TEMPLATE_PATTERN = Pattern.compile(
            "^TEMPLATE\\s+((.*?)(?=^(PARAMETER|MESSAGE|SYSTEM|FROM|ADAPTER|LICENSE))|(.*$))",
            Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
    private static final Pattern MESSAGE_PATTERN = Pattern.compile("^MESSAGE\\s+(user|system|assistant)?\\s+(.*)$",
            Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
    private static final Pattern PARAMETER_PATTERN = Pattern.compile("^PARAMETER\\s+(.*?)\\s+(.*?)$",
            Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    private static final Pattern SYSTEM_PATTERN = Pattern.compile("^SYSTEM\\s+(.*?)$",
            Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
    private static final Pattern ADAPTER_PATTERN = Pattern.compile("^ADAPTER\\s+(.*?)\\s*$",
            Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
    private static final Pattern LICENSE_PATTERN = Pattern.compile(
            "^LICENSE\\s+((.*?)(?=^(PARAMETER|MESSAGE|SYSTEM|FROM|ADAPTER|TEMPLATE))|(.*$))",
            Pattern.DOTALL | Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);

    /**
     * String representation of the model file.
     * 
     * @see <a href=
     *      "https://github.com/ollama/ollama/blob/main/docs/modelfile.md#format">Model
     *      file</a>
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FROM ").append(from).append("\n");
        if (adapter != null) {
            builder.append("ADAPTER ").append(adapter).append("\n");
        }
        if (template != null) {
            builder.append("TEMPLATE ").append(template).append("\n");
        }
        if (system != null) {
            builder.append("SYSTEM ").append(system).append("\n");
        }
        if (messages != null) {
            messages.forEach(
                    m -> builder.append("MESSAGE ").append(m.role()).append(" ").append(m.content()).append("\n"));
        }
        if (parameters != null) {
            parameters.forEach((k, v) -> {

                if (List.class.isAssignableFrom(v.getClass())) {
                    @SuppressWarnings("unchecked")
                    List<String> list = (List<String>) v;
                    list.forEach(
                            value -> builder.append("PARAMETER ").append(k).append(" ").append(value)
                                    .append("\n"));
                } else {
                    builder.append("PARAMETER ").append(k).append(" ").append(v).append("\n");
                }

            });
        }

        if (license != null) {
            builder.append("LICENSE ").append(license).append("\n");
        }

        return builder.toString();
    }

    /**
     * Enumeration of model file paramter keys.
     * 
     * @see <a href=
     *      "https://github.com/ollama/ollama/blob/main/docs/modelfile.md#parameters">Parameters</a>
     */
    public enum Key implements ParameterKey {

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

        /**
         * Returns the {@code Key} for the given value.
         * 
         * @param value The value
         * @return the {@code Key} for the given value or {@code null} if not found
         */
        public static Key of(String value) {
            return Arrays.stream(values()).filter(v -> v.getValue().equals(value)).findFirst()
                    .orElse(null);
        }
    }

    /**
     * Parse the model file.
     * 
     * @param modelfile The model file
     * @return the {@code ModelFile}
     * @throws InvalidModelFileException If the model file is invalid
     */
    public static ModelFile parse(String modelfile) throws InvalidModelFileException {
        Objects.requireNonNull(modelfile, "modelfile cannot be null");
        String from = null;
        String template = null;
        Map<ParameterKey, Object> parameters = new HashMap<>();
        List<String> stops = new ArrayList<>();
        String system = null;
        String adapter = null;
        String license = null;
        List<Message> messages = new ArrayList<>();

        Matcher fromMatcher = FROM_PATTERN.matcher(modelfile);
        if (fromMatcher.find()) {
            from = fromMatcher.group(1);
        }

        Matcher templateMatcher = TEMPLATE_PATTERN.matcher(modelfile);
        if (templateMatcher.find()) {
            template = templateMatcher.group(1);
        }

        Matcher messageMatcher = MESSAGE_PATTERN.matcher(modelfile);
        while (messageMatcher.find()) {
            Role role = Role.of(messageMatcher.group(1));
            if (role != null) {
                messages.add(new Message(role, messageMatcher.group(2), List.of()));
            }
        }

        Matcher parameterMatcher = PARAMETER_PATTERN.matcher(modelfile);
        while (parameterMatcher.find()) {
            Key key = Key.of(parameterMatcher.group(1));
            if (key != null) {
                if (key == Key.STOP) {
                    stops.add(parameterMatcher.group(2));
                } else {
                    parameters.put(key, parameterMatcher.group(2));
                }
            }
        }

        Matcher licenseMatcher = LICENSE_PATTERN.matcher(modelfile);
        if (licenseMatcher.find()) {
            license = licenseMatcher.group(1);
        }

        Matcher systemMatcher = SYSTEM_PATTERN.matcher(modelfile);
        if (systemMatcher.find()) {
            system = systemMatcher.group(1);
        }

        Matcher adapterMatcher = ADAPTER_PATTERN.matcher(modelfile);
        if (adapterMatcher.find()) {
            adapter = adapterMatcher.group(1);
        }

        parameters.put(Key.STOP, stops);

        if (from == null) {
            throw new InvalidModelFileException("FROM is required");
        }

        return new ModelFile(from, adapter, template, system, messages, parameters, license);
    }

    /**
     * Parse the model file.
     * 
     * @param path The path to the model file
     * @return the {@code ModelFile}
     * @throws InvalidModelFileException If the model file is invalid
     * @throws UncheckedIOException      If an I/O error occurs
     */
    public static ModelFile parse(Path path) throws InvalidModelFileException, UncheckedIOException {
        Objects.requireNonNull(path, "path cannot be null");
        String modelfile = null;
        try {
            modelfile = Files.readString(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return parse(modelfile);
    }

    public static Builder from(String from) {
        return new DefaultModelFileBuilder(from);
    }

    public static Builder from(Path path) {
        Objects.requireNonNull(path, "path cannot be null");
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("path does not exist");
        }

        return from(path.toString());

    }

    public Float temperature() {
        return (Float) parameters.get(Key.TEMPERATURE);
    }

    public Integer seed() {
        return (Integer) parameters.get(Key.SEED);
    }

    public Float mirostat() {
        return (Float) parameters.get(Key.MIROSTAT);
    }

    public Float mirostatTau() {
        return (Float) parameters.get(Key.MIROSTAT_TAU);
    }

    public Float mirostatEta() {
        return (Float) parameters.get(Key.MIROSTAT_ETA);
    }

    public Integer numCtx() {
        return (Integer) parameters.get(Key.NUM_CTX);
    }

    public Integer repeatLastN() {
        return (Integer) parameters.get(Key.REPEAT_LAST_N);
    }

    public Float repeatPenalty() {
        return (Float) parameters.get(Key.REPEAT_PENALTY);
    }

    public Integer topK() {
        return (Integer) parameters.get(Key.TOP_K);
    }

    public Float topP() {
        return (Float) parameters.get(Key.TOP_P);
    }

    public Float tfsZ() {
        return (Float) parameters.get(Key.TFS_Z);
    }

    @SuppressWarnings("unchecked")
    public List<String> stop() {
        return (List<String>) parameters.get(Key.STOP);
    }

    public interface Builder {

        Builder template(String template);

        Builder system(String system);

        Builder adapter(String adapter);

        Builder adapter(Path path);

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
        private Map<ParameterKey, Object> parameters = new HashMap<>();
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
        public Builder adapter(Path path) {
            Objects.requireNonNull(path, "path cannot be null");
            if (!Files.exists(path)) {
                throw new IllegalArgumentException("path does not exist");
            }
            return adapter(path.toString());
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
            return new ModelFile(from, adapter, template, system, messages, parameters, license);
        }

    }

}
