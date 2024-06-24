package io.github.glynch.jollama;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonValue;

import io.github.glynch.jollama.modelfile.ModelFile;

public class Options extends AbstractMap<ParameterKey, Object> {

    private final Map<ParameterKey, Object> optionsMap = new HashMap<>();

    private Options(Map<ParameterKey, Object> optionsMap) {
        this.optionsMap.putAll(optionsMap);
    }

    public Options(Options options) {
        this.optionsMap.putAll(options);
    }

    @Override
    public Set<Entry<ParameterKey, Object>> entrySet() {
        return optionsMap.entrySet();
    }

    @Override
    public Object put(ParameterKey key, Object value) {
        throw new UnsupportedOperationException("'put' is not supported on Options");
    }

    public Integer numKeep() {
        return (Integer) optionsMap.get(Key.NUM_KEEP);
    }

    public Integer seed() {
        return (Integer) optionsMap.get(ModelFile.Key.SEED);
    }

    public Integer numPredict() {
        return (Integer) optionsMap.get(ModelFile.Key.NUM_PREDICT);
    }

    public Integer topK() {
        return (Integer) optionsMap.get(ModelFile.Key.TOP_K);
    }

    public Float topP() {
        return (Float) optionsMap.get(ModelFile.Key.TOP_P);
    }

    public Float getTfsZ() {
        return (Float) optionsMap.get(ModelFile.Key.TFS_Z);
    }

    public Float typicalP() {
        return (Float) optionsMap.get(Key.TYPICAL_P);
    }

    public Integer repeatLastN() {
        return (Integer) optionsMap.get(ModelFile.Key.REPEAT_LAST_N);
    }

    public Float temperature() {
        return (Float) optionsMap.get(ModelFile.Key.TEMPERATURE);
    }

    public Float repeatPenalty() {
        return (Float) optionsMap.get(ModelFile.Key.REPEAT_PENALTY);
    }

    public Float presencePenalty() {
        return (Float) optionsMap.get(Key.PRESENCE_PENALTY);
    }

    public Float frequencyPenalty() {
        return (Float) optionsMap.get(Key.FREQUENCY_PENALTY);
    }

    public Integer miroStat() {
        return (Integer) optionsMap.get(ModelFile.Key.MIROSTAT);
    }

    public Float miroStatTau() {
        return (Float) optionsMap.get(ModelFile.Key.MIROSTAT_TAU);
    }

    public Float miroStatEta() {
        return (Float) optionsMap.get(ModelFile.Key.MIROSTAT_ETA);
    }

    public Boolean penalizeNewline() {
        return (Boolean) optionsMap.get(Key.PENALIZE_NEWLINE);
    }

    @SuppressWarnings("unchecked")
    public List<String> stop() {
        return (List<String>) optionsMap.get(ModelFile.Key.STOP);
    }

    public Boolean numa() {
        return (Boolean) optionsMap.get(Key.NUMA);
    }

    public Integer numCtx() {
        return (Integer) optionsMap.get(ModelFile.Key.NUM_CTX);
    }

    public Integer numBatch() {
        return (Integer) optionsMap.get(Key.NUM_BATCH);
    }

    public Integer numGpu() {
        return (Integer) optionsMap.get(Key.NUM_GPU);
    }

    public Integer mainGpu() {
        return (Integer) optionsMap.get(Key.MAIN_GPU);
    }

    public Boolean lowVram() {
        return (Boolean) optionsMap.get(Key.LOW_VRAM);
    }

    public Boolean f16Kv() {
        return (Boolean) optionsMap.get(Key.F16_KV);
    }

    public Boolean vocabOnly() {
        return (Boolean) optionsMap.get(Key.VOCAB_ONLY);
    }

    public Boolean useMmap() {
        return (Boolean) optionsMap.get(Key.USE_MMAP);
    }

    public Boolean useMlock() {
        return (Boolean) optionsMap.get(Key.USE_MLOCK);
    }

    public Integer numThread() {
        return (Integer) optionsMap.get(Key.NUM_THREAD);
    }

    public static Options create() {
        return Options.builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder of {@linkplain Options options}.
     */
    public static class Builder {
        private final Map<ParameterKey, Object> options = new HashMap<>();

        public Builder() {

        }

        public Builder(Options options) {
            Objects.requireNonNull(options, "options cannot be null");
            this.options.putAll(options);
        }

        /**
         * Sets the number of predictions to generate.
         * 
         * @param numKeep The number of predictions
         * @return this builder
         */
        public Builder numKeep(Integer numKeep) {
            Objects.requireNonNull(numKeep, "numKeep cannot be null");
            options.put(Key.NUM_KEEP, numKeep);
            return this;
        }

        /**
         * Sets the random number seed. Used for reproducibility.
         * 
         * @param seed The random number seed
         * @return this builder
         */
        public Builder seed(Integer seed) {
            Objects.requireNonNull(seed, "seed cannot be null");
            options.put(ModelFile.Key.SEED, seed);
            return this;
        }

        /**
         * Sets the maximum number of predictions to generate.
         * 
         * @param numPredict The number of token predictions
         * @return this builder
         */
        public Builder numPredict(Integer numPredict) {
            options.put(ModelFile.Key.NUM_PREDICT, numPredict);
            return this;
        }

        /**
         * Limits the number of highest probability tokens to consider at each step of
         * the generation.
         * 
         * @param topK The number of highest probability tokens
         * @return this builder
         */
        public Builder topK(Integer topK) {
            Objects.requireNonNull(topK, "topK cannot be null");
            options.put(ModelFile.Key.TOP_K, topK);
            return this;
        }

        /**
         * Controls the cumulative probability of the generated tokens.
         * Also known as nucleus sampling.
         * 
         * @param topP The cumulative probability
         * @return this builder
         * 
         * @see <a href="https://arxiv.org/abs/1904.09751">Nucleus Sampling</a>
         * @see <a href="https://en.wikipedia.org/wiki/Top-p_sampling">Top-p
         *      sampling</a>
         */
        public Builder topP(Float topP) {
            Objects.requireNonNull(topP, "topP cannot be null");
            options.put(ModelFile.Key.TOP_P, topP);
            return this;
        }

        /**
         * Reduces the impact of less probable tokens.
         * Adjusts the number of logits (token probabilties) to consider.
         * A value of 1.0 disables this feature.
         * 
         * @param tfsZ The number of logits
         * @return this builder
         */
        public Builder tfsZ(Float tfsZ) {
            Objects.requireNonNull(tfsZ, "tfsZ cannot be null");
            options.put(ModelFile.Key.TFS_Z, tfsZ);
            return this;
        }

        public Builder typicalP(Float typicalP) {
            Objects.requireNonNull(typicalP, "typicalP cannot be null");
            options.put(Key.TYPICAL_P, typicalP);
            return this;
        }

        /**
         * The last N tokens to consider for penalizing repetition.
         * 
         * @param repeatLastN The number of tokens
         * @return this builder
         */
        public Builder repeatLastN(Integer repeatLastN) {
            Objects.requireNonNull(repeatLastN, "repeatLastN cannot be null");
            options.put(ModelFile.Key.REPEAT_LAST_N, repeatLastN);
            return this;
        }

        /**
         * The temperature is a hyperparameter that controls the randomness of the
         * geneerated text.
         * 
         * A value of {@code 0.0} is deterministic.
         * 
         * @param temperature The temperature
         * @return this builder
         */
        public Builder temperature(Float temperature) {
            options.put(ModelFile.Key.TEMPERATURE, temperature);
            return this;
        }

        /**
         * Penalizes repetition of tokens.
         * 
         * @param repeatPenalty The penalty
         * @return this builder
         */
        public Builder repeatPenalty(Float repeatPenalty) {
            Objects.requireNonNull(repeatPenalty, "repeatPenalty cannot be null");
            options.put(ModelFile.Key.REPEAT_PENALTY, repeatPenalty);
            return this;
        }

        public Builder presencePenalty(Float presencePenalty) {
            Objects.requireNonNull(presencePenalty, "presencePenalty cannot be null");
            options.put(Key.PRESENCE_PENALTY, presencePenalty);
            return this;
        }

        public Builder frequencyPenalty(Float frequencyPenalty) {
            Objects.requireNonNull(frequencyPenalty, "frequencyPenalty cannot be null");
            options.put(Key.FREQUENCY_PENALTY, frequencyPenalty);
            return this;
        }

        /**
         * Enable mirostat sampling.
         * Conrolling the perplexity during text generation.
         * 
         * 0 - Disabled (default)
         * 1 - Mirostat
         * 2 - Mirostat 2.0
         * 
         * @param miroStat
         * @return this builder
         */
        public Builder miroStat(Integer miroStat) {
            Objects.requireNonNull(miroStat, "miroStat cannot be null");
            options.put(ModelFile.Key.MIROSTAT, miroStat);
            return this;
        }

        /**
         * Mirostat target entropy parameter.
         * 
         * @param miroStatTau Desired perpleity value.
         * @return this builder
         */
        public Builder miroStatTau(Float miroStatTau) {
            Objects.requireNonNull(miroStatTau, "miroStatTau cannot be null");
            options.put(ModelFile.Key.MIROSTAT_TAU, miroStatTau);
            return this;
        }

        /**
         * Mirostat learning rate.
         * 
         * @param miroStatEta The learning rate
         * @return this builder
         */
        public Builder miroStatEta(Float miroStatEta) {
            Objects.requireNonNull(miroStatEta, "miroStatEta cannot be null");
            options.put(ModelFile.Key.MIROSTAT_ETA, miroStatEta);
            return this;
        }

        /**
         * Whether to penalize newlines when applying repeat penalty.
         * 
         * @param penalizeNewline Whether to penalize newlines
         * @return this builder
         */
        public Builder penalizeNewline(Boolean penalizeNewline) {
            Objects.requireNonNull(penalizeNewline, "penalizeNewline cannot be null");
            options.put(Key.PENALIZE_NEWLINE, penalizeNewline);
            return this;
        }

        /**
         * The tokens to stop generation at.
         * 
         * @param stop The tokens to stop at
         * @return this builder
         */
        public Builder stop(List<String> stop) {
            Objects.requireNonNull(stop, "stop cannot be null");
            options.put(ModelFile.Key.STOP, stop);
            return this;
        }

        /**
         * Whether to use NUMA (Non Uniform Memory Access) memory allocation.
         * 
         * @param numa Whether to use NUMA
         * @return this builder
         */
        public Builder numa(Boolean numa) {
            Objects.requireNonNull(numa, "numa cannot be null");
            options.put(Key.NUMA, numa);
            return this;
        }

        /**
         * The number of context tokens to use.
         * 
         * @param numCtx The number of context tokens
         * @return this builder
         */
        public Builder numCtx(Integer numCtx) {
            Objects.requireNonNull(numCtx, "numCtx cannot be null");
            options.put(ModelFile.Key.NUM_CTX, numCtx);
            return this;
        }

        /**
         * Set the batch size for prompt processing.
         * 
         * @param numBatch The batch size
         * @return this builder
         */
        public Builder numBatch(Integer numBatch) {
            Objects.requireNonNull(numBatch, "numBatch cannot be null");
            options.put(Key.NUM_BATCH, numBatch);
            return this;
        }

        /**
         * Set the layers to send to GPU.
         *
         *
         * @param numGpu The number of layers to send to GPU
         * @return this builder
         */
        public Builder numGpu(Integer numGpu) {
            Objects.requireNonNull(numGpu, "numGpu cannot be null");
            options.put(Key.NUM_GPU, numGpu);
            return this;
        }

        /**
         * When using multiple GPUs this option controls which GPU is used for small
         * tensors for which the overhead of splitting the computation across all GPUs
         * is not worthwhile. (Requires cuBLAS)
         * 
         * @param mainGpu The main GPU
         * @return this builder
         */
        public Builder mainGpu(Integer mainGpu) {
            Objects.requireNonNull(mainGpu, "mainGpu cannot be null");
            options.put(Key.MAIN_GPU, mainGpu);
            return this;
        }

        /**
         * Do not allocate a VRAM scratch buffer for holding temporary results. Reduces
         * VRAM usage at the cost of performance, particularly prompt processing speed.
         * (Requires cuBLAS).
         * 
         * @param lowVram Whether to use low VRAM
         * @return this builder
         */
        public Builder lowVram(Boolean lowVram) {
            Objects.requireNonNull(lowVram, "lowVram cannot be null");
            options.put(Key.LOW_VRAM, lowVram);
            return this;
        }

        public Builder f16Kv(Boolean f16Kv) {
            Objects.requireNonNull(f16Kv, "f16Kv cannot be null");
            options.put(Key.F16_KV, f16Kv);
            return this;
        }

        public Builder vocabOnly(Boolean vocabOnly) {
            Objects.requireNonNull(vocabOnly, "vocabOnly cannot be null");
            options.put(Key.VOCAB_ONLY, vocabOnly);
            return this;
        }

        /**
         * Whether to use memory mapping for the model. (default)
         * If the model is larger than available RAM, set to false.
         *
         * @param useMmap Whether to use memory mapping
         * @return this builder
         */
        public Builder useMmap(Boolean useMmap) {
            Objects.requireNonNull(useMmap, "useMmap cannot be null");
            options.put(Key.USE_MMAP, useMmap);
            return this;
        }

        /**
         * Whether to use mlock to lock the model in memory.
         * Preventing it from being swapped out.
         * 
         * @param useMlock Whether to use mlock
         * @return this builder
         */
        public Builder useMlock(Boolean useMlock) {
            Objects.requireNonNull(useMlock, "useMlock cannot be null");
            options.put(Key.USE_MLOCK, useMlock);
            return this;
        }

        /**
         * The number of threads to use during computation.
         * For better performance set to the number of physical CPU's.
         * 
         * @param numThread The number of threads
         * @return this builder
         */
        public Builder numThread(Integer numThread) {
            Objects.requireNonNull(numThread, "numThread cannot be null");
            options.put(Key.NUM_THREAD, numThread);
            return this;
        }

        /**
         * Builds and returns new {@link Options} instance.
         * 
         * @return a new {@code Options}
         */
        public Options build() {
            return new Options(options);
        }

    }

    public enum Key implements ParameterKey {
        NUM_KEEP("num_keep"),
        NUM_PREDICT("num_predict"),
        TYPICAL_P("typical_p"),
        PRESENCE_PENALTY("presence_penalty"),
        FREQUENCY_PENALTY("frequency_penalty"),
        PENALIZE_NEWLINE("penalize_newline"),
        STOP("stop"),
        NUMA("numa"),
        NUM_BATCH("num_batch"),
        NUM_GPU("num_gpu"),
        MAIN_GPU("main_gpu"),
        LOW_VRAM("low_vram"),
        F16_KV("f16_kv"),
        VOCAB_ONLY("vocab_only"),
        USE_MMAP("use_mmap"),
        USE_MLOCK("use_mlock"),
        NUM_THREAD("num_thread");

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
            Objects.requireNonNull(value, "value cannot be null");
            return Arrays.stream(values()).filter(v -> v.getValue().equals(value)).findFirst()
                    .orElse(null);
        }
    }

}