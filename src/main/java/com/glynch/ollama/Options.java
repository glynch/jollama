package com.glynch.ollama;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonValue;
import com.glynch.ollama.modelfile.ModelFile;

public class Options extends AbstractMap<String, Object> {

    private final Map<String, Object> options = new HashMap<>();

    private Options(Map<String, Object> options) {
        this.options.putAll(options);
    }

    public Options(Options options) {
        this.options.putAll(options);
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return options.entrySet();
    }

    @Override
    public Object put(String key, Object value) {
        throw new UnsupportedOperationException("'put' is not supported on Options");
    }

    public Integer getNumKeep() {
        return (Integer) options.get(Key.NUM_KEEP.getValue());
    }

    public Integer getSeed() {
        return (Integer) options.get(ModelFile.Key.SEED.getValue());
    }

    public Integer getNumPredict() {
        return (Integer) options.get(ModelFile.Key.NUM_PREDICT.getValue());
    }

    public Integer getTopK() {
        return (Integer) options.get(ModelFile.Key.TOP_K.getValue());
    }

    public Float getTopP() {
        return (Float) options.get(ModelFile.Key.TOP_P.getValue());
    }

    public Float getTfsZ() {
        return (Float) options.get(ModelFile.Key.TFS_Z.getValue());
    }

    public Float getTypicalP() {
        return (Float) options.get(Key.TYPICAL_P.getValue());
    }

    public Integer getRepeatLastN() {
        return (Integer) options.get(ModelFile.Key.REPEAT_LAST_N.getValue());
    }

    public Float getTemperature() {
        return (Float) options.get(ModelFile.Key.TEMPERATURE.getValue());
    }

    public Float getRepeatPenalty() {
        return (Float) options.get(ModelFile.Key.REPEAT_PENALTY.getValue());
    }

    public Float getPresencePenalty() {
        return (Float) options.get(Key.PRESENCE_PENALTY.getValue());
    }

    public Float getFrequencyPenalty() {
        return (Float) options.get(Key.FREQUENCY_PENALTY.getValue());
    }

    public Integer getMiroStat() {
        return (Integer) options.get(ModelFile.Key.MIROSTAT.getValue());
    }

    public Float getMiroStatTau() {
        return (Float) options.get(ModelFile.Key.MIROSTAT_TAU.getValue());
    }

    public Boolean getPenalizeNewline() {
        return (Boolean) options.get(Key.PENALIZE_NEWLINE.getValue());
    }

    String getStop() {
        return (String) options.get(ModelFile.Key.STOP.getValue());
    }

    public Boolean getNuma() {
        return (Boolean) options.get(Key.NUMA.getValue());
    }

    public Integer getNumCtx() {
        return (Integer) options.get(ModelFile.Key.NUM_CTX.getValue());
    }

    public Integer getNumBatch() {
        return (Integer) options.get(Key.NUM_BATCH.getValue());
    }

    public Integer getNumGpu() {
        return (Integer) options.get(Key.NUM_GPU.getValue());
    }

    public Integer getMainGpu() {
        return (Integer) options.get(Key.MAIN_GPU.getValue());
    }

    public Boolean getLowVram() {
        return (Boolean) options.get(Key.LOW_VRAM.getValue());
    }

    public Boolean getF16Kv() {
        return (Boolean) options.get(Key.F16_KV.getValue());
    }

    public Boolean getVocabOnly() {
        return (Boolean) options.get(Key.VOCAB_ONLY.getValue());
    }

    public Boolean getUseMmap() {
        return (Boolean) options.get(Key.USE_MMAP.getValue());
    }

    public Boolean getUseMlock() {
        return (Boolean) options.get(Key.USE_MLOCK.getValue());
    }

    public Integer getNumThread() {
        return (Integer) options.get(Key.NUM_THREAD.getValue());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Map<String, Object> options = new HashMap<>();

        public Builder() {

        }

        public Builder(Options options) {
            this.options.putAll(options);
        }

        public Builder numKeep(Integer numKeep) {
            options.put(Key.NUM_KEEP.getValue(), numKeep);
            return this;
        }

        public Builder seed(Integer seed) {
            options.put(ModelFile.Key.SEED.getValue(), seed);
            return this;
        }

        public Builder numPredict(Integer numPredict) {
            options.put(ModelFile.Key.NUM_PREDICT.getValue(), numPredict);
            return this;
        }

        public Builder topK(Integer topK) {
            options.put(ModelFile.Key.TOP_K.getValue(), topK);
            return this;
        }

        public Builder topP(Float topP) {
            options.put(ModelFile.Key.TOP_P.getValue(), topP);
            return this;
        }

        public Builder tfsZ(Float tfsZ) {
            options.put(ModelFile.Key.TFS_Z.getValue(), tfsZ);
            return this;
        }

        public Builder typicalP(Float typicalP) {
            options.put(Key.TYPICAL_P.getValue(), typicalP);
            return this;
        }

        public Builder repeatLastN(Integer repeatLastN) {
            options.put(ModelFile.Key.REPEAT_LAST_N.getValue(), repeatLastN);
            return this;
        }

        public Builder temperature(Float temperature) {
            options.put(ModelFile.Key.TEMPERATURE.getValue(), temperature);
            return this;
        }

        public Builder repeatPenalty(Float repeatPenalty) {
            options.put(ModelFile.Key.REPEAT_PENALTY.getValue(), repeatPenalty);
            return this;
        }

        public Builder presencePenalty(Float presencePenalty) {
            options.put(Key.PRESENCE_PENALTY.getValue(), presencePenalty);
            return this;
        }

        public Builder frequencyPenalty(Float frequencyPenalty) {
            options.put(Key.FREQUENCY_PENALTY.getValue(), frequencyPenalty);
            return this;
        }

        public Builder miroStat(Integer miroStat) {
            options.put(ModelFile.Key.MIROSTAT.getValue(), miroStat);
            return this;
        }

        public Builder miroStatTau(Float miroStatTau) {
            options.put(ModelFile.Key.MIROSTAT_TAU.getValue(), miroStatTau);
            return this;
        }

        public Builder penalizeNewline(Boolean penalizeNewline) {
            options.put(Key.PENALIZE_NEWLINE.getValue(), penalizeNewline);
            return this;
        }

        public Builder stop(String stop) {
            options.put(ModelFile.Key.STOP.getValue(), stop);
            return this;
        }

        public Builder numa(Boolean numa) {
            options.put(Key.NUMA.getValue(), numa);
            return this;
        }

        public Builder numCtx(Integer numCtx) {
            options.put(ModelFile.Key.NUM_CTX.getValue(), numCtx);
            return this;
        }

        public Builder numBatch(Integer numBatch) {
            options.put(Key.NUM_BATCH.getValue(), numBatch);
            return this;
        }

        public Builder numGpu(Integer numGpu) {
            options.put(Key.NUM_GPU.getValue(), numGpu);
            return this;
        }

        public Builder mainGpu(Integer mainGpu) {
            options.put(Key.MAIN_GPU.getValue(), mainGpu);
            return this;
        }

        public Builder lowVram(Boolean lowVram) {
            options.put(Key.LOW_VRAM.getValue(), lowVram);
            return this;
        }

        public Builder f16Kv(Boolean f16Kv) {
            options.put(Key.F16_KV.getValue(), f16Kv);
            return this;
        }

        public Builder vocabOnly(Boolean vocabOnly) {
            options.put(Key.VOCAB_ONLY.getValue(), vocabOnly);
            return this;
        }

        public Builder useMmap(Boolean useMmap) {
            options.put(Key.USE_MMAP.getValue(), useMmap);
            return this;
        }

        public Builder useMlock(Boolean useMlock) {
            options.put(Key.USE_MLOCK.getValue(), useMlock);
            return this;
        }

        public Builder numThread(Integer numThread) {
            options.put(Key.NUM_THREAD.getValue(), numThread);
            return this;
        }

        public Options build() {
            return new Options(options);
        }

    }

    public enum Key {
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

        public static Key of(String value) {
            return Arrays.stream(values()).filter(v -> v.getValue().equals(value)).findFirst()
                    .orElse(null);
        }
    }

}