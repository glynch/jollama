package com.glynch.ollama;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonValue;

public class Options extends AbstractMap<Options.Key, Object> {

    private final Map<Options.Key, Object> options = new HashMap<>();

    public Options() {
    }

    public Options(Options options) {
        this.options.putAll(options);
    }

    @Override
    public Set<Entry<Options.Key, Object>> entrySet() {
        return options.entrySet();
    }

    @Override
    public Object put(Options.Key key, Object value) {
        throw new UnsupportedOperationException("'put' is not supported on Options");
    }

    public Integer getNumKeep() {
        return (Integer) options.get(Key.NUM_KEEP);
    }

    public void setNumKeep(Integer numKeep) {
        options.put(Key.NUM_KEEP, numKeep);
    }

    public Integer getSeed() {
        return (Integer) options.get(Key.SEED);
    }

    public void setSeed(Integer seed) {
        options.put(Key.SEED, seed);
    }

    public Integer getNumPredict() {
        return (Integer) options.get(Key.NUM_PREDICT);
    }

    public void setNumPredict(Integer numPredict) {
        options.put(Key.NUM_PREDICT, numPredict);
    }

    public Integer getTopK() {
        return (Integer) options.get(Key.TOP_K);
    }

    public void setTopK(Integer topK) {
        options.put(Key.TOP_K, topK);
    }

    public Float getTopP() {
        return (Float) options.get(Key.TOP_P);
    }

    public void setTopP(Float topP) {
        options.put(Key.TOP_P, topP);
    }

    public Float getTfsZ() {
        return (Float) options.get(Key.TFS_Z);
    }

    public void setTfsZ(Float tfsZ) {
        options.put(Key.TFS_Z, tfsZ);
    }

    public Float getTypicalP() {
        return (Float) options.get(Key.TYPICAL_P);
    }

    public void setTypicalP(Float typicalP) {
        options.put(Key.TYPICAL_P, typicalP);
    }

    public Integer getRepeatLastN() {
        return (Integer) options.get(Key.REPEAT_LAST_N);
    }

    public void setRepeatLastN(Integer repeatLastN) {
        options.put(Key.REPEAT_LAST_N, repeatLastN);
    }

    public Float getTemperature() {
        return (Float) options.get(Key.TEMPERATURE);
    }

    public void setTemperature(Float temperature) {
        options.put(Key.TEMPERATURE, temperature);
    }

    public Float getRepeatPenalty() {
        return (Float) options.get(Key.REPEAT_PENALTY);
    }

    public void setRepeatPenalty(Float repeatPenalty) {
        options.put(Key.REPEAT_PENALTY, repeatPenalty);
    }

    public Float getPresencePenalty() {
        return (Float) options.get(Key.PRESENCE_PENALTY);
    }

    public void setPresencePenalty(Float presencePenalty) {
        options.put(Key.PRESENCE_PENALTY, presencePenalty);
    }

    public Float getFrequencyPenalty() {
        return (Float) options.get(Key.FREQUENCY_PENALTY);
    }

    public void setFrequencyPenalty(Float frequencyPenalty) {
        options.put(Key.FREQUENCY_PENALTY, frequencyPenalty);
    }

    public Integer getMiroStat() {
        return (Integer) options.get(Key.MIROSTAT);
    }

    public void setMiroStat(Integer miroStat) {
        options.put(Key.MIROSTAT, miroStat);
    }

    public Float getMiroStatTau() {
        return (Float) options.get(Key.MIROSTAT_TAU);
    }

    public void setMiroStatTau(Float miroStatTau) {
        options.put(Key.MIROSTAT_TAU, miroStatTau);
    }

    public Boolean getPenalizeNewline() {
        return (Boolean) options.get(Key.PENALIZE_NEWLINE);
    }

    public void setPenalizeNewline(Boolean penalizeNewline) {
        options.put(Key.PENALIZE_NEWLINE, penalizeNewline);
    }

    String getStop() {
        return (String) options.get(Key.STOP);
    }

    void setStop(String stop) {
        options.put(Key.STOP, stop);
    }

    public Boolean getNuma() {
        return (Boolean) options.get(Key.NUMA);
    }

    public void setNuma(Boolean numa) {
        options.put(Key.NUMA, numa);
    }

    public Integer getNumCtx() {
        return (Integer) options.get(Key.NUM_CTX);
    }

    public void setNumCtx(Integer numCtx) {
        options.put(Key.NUM_CTX, numCtx);
    }

    public Integer getNumBatch() {
        return (Integer) options.get(Key.NUM_BATCH);
    }

    public void setNumBatch(Integer numBatch) {
        options.put(Key.NUM_BATCH, numBatch);
    }

    public Integer getNumGpu() {
        return (Integer) options.get(Key.NUM_GPU);
    }

    public void setNumGpu(Integer numGpu) {
        options.put(Key.NUM_GPU, numGpu);
    }

    public Integer getMainGpu() {
        return (Integer) options.get(Key.MAIN_GPU);
    }

    public void setMainGpu(Integer mainGpu) {
        options.put(Key.MAIN_GPU, mainGpu);
    }

    public Boolean getLowVram() {
        return (Boolean) options.get(Key.LOW_VRAM);
    }

    public void setLowVram(Boolean lowVram) {
        options.put(Key.LOW_VRAM, lowVram);
    }

    public Boolean getF16Kv() {
        return (Boolean) options.get(Key.F16_KV);
    }

    public void setF16Kv(Boolean f16Kv) {
        options.put(Key.F16_KV, f16Kv);
    }

    public Boolean getVocabOnly() {
        return (Boolean) options.get(Key.VOCAB_ONLY);
    }

    public void setVocabOnly(Boolean vocabOnly) {
        options.put(Key.VOCAB_ONLY, vocabOnly);
    }

    public Boolean getUseMmap() {
        return (Boolean) options.get(Key.USE_MMAP);
    }

    public void setUseMmap(Boolean useMmap) {
        options.put(Key.USE_MMAP, useMmap);
    }

    public Boolean getUseMlock() {
        return (Boolean) options.get(Key.USE_MLOCK);
    }

    public void setUseMlock(Boolean useMlock) {
        options.put(Key.USE_MLOCK, useMlock);
    }

    public Integer getNumThread() {
        return (Integer) options.get(Key.NUM_THREAD);
    }

    public void setNumThread(Integer numThread) {
        options.put(Key.NUM_THREAD, numThread);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Options options) {
        return new Builder(options);
    }

    public static class Builder {
        private final Options options;

        public Builder() {
            this.options = new Options();
        }

        public Builder(Options options) {
            this.options = new Options(options);
        }

        public Builder numKeep(Integer numKeep) {
            options.setNumKeep(numKeep);
            return this;
        }

        public Builder seed(Integer seed) {
            options.setSeed(seed);
            return this;
        }

        public Builder numPredict(Integer numPredict) {
            options.setNumPredict(numPredict);
            return this;
        }

        public Builder topK(Integer topK) {
            options.setTopK(topK);
            return this;
        }

        public Builder topP(Float topP) {
            options.setTopP(topP);
            return this;
        }

        public Builder tfsZ(Float tfsZ) {
            options.setTfsZ(tfsZ);
            return this;
        }

        public Builder typicalP(Float typicalP) {
            options.setTypicalP(typicalP);
            return this;
        }

        public Builder repeatLastN(Integer repeatLastN) {
            options.setRepeatLastN(repeatLastN);
            return this;
        }

        public Builder temperature(Float temperature) {
            options.setTemperature(temperature);
            return this;
        }

        public Builder repeatPenalty(Float repeatPenalty) {
            options.setRepeatPenalty(repeatPenalty);
            return this;
        }

        public Builder presencePenalty(Float presencePenalty) {
            options.setPresencePenalty(presencePenalty);
            return this;
        }

        public Builder frequencyPenalty(Float frequencyPenalty) {
            options.setFrequencyPenalty(frequencyPenalty);
            return this;
        }

        public Builder miroStat(Integer miroStat) {
            options.setMiroStat(miroStat);
            return this;
        }

        public Builder miroStatTau(Float miroStatTau) {
            options.setMiroStatTau(miroStatTau);
            return this;
        }

        public Builder penalizeNewline(Boolean penalizeNewline) {
            options.setPenalizeNewline(penalizeNewline);
            return this;
        }

        public Builder stop(String stop) {
            options.setStop(stop);
            return this;
        }

        public Builder numa(Boolean numa) {
            options.setNuma(numa);
            return this;
        }

        public Builder numCtx(Integer numCtx) {
            options.setNumCtx(numCtx);
            return this;
        }

        public Builder numBatch(Integer numBatch) {
            options.setNumBatch(numBatch);
            return this;
        }

        public Builder numGpu(Integer numGpu) {
            options.setNumGpu(numGpu);
            return this;
        }

        public Builder mainGpu(Integer mainGpu) {
            options.setMainGpu(mainGpu);
            return this;
        }

        public Builder lowVram(Boolean lowVram) {
            options.setLowVram(lowVram);
            return this;
        }

        public Builder f16Kv(Boolean f16Kv) {
            options.setF16Kv(f16Kv);
            return this;
        }

        public Builder vocabOnly(Boolean vocabOnly) {
            options.setVocabOnly(vocabOnly);
            return this;
        }

        public Builder useMmap(Boolean useMmap) {
            options.setUseMmap(useMmap);
            return this;
        }

        public Builder useMlock(Boolean useMlock) {
            options.setUseMlock(useMlock);
            return this;
        }

        public Builder numThread(Integer numThread) {
            options.setNumThread(numThread);
            return this;
        }

        public Options build() {
            return options;
        }

    }

    public enum Key {
        NUM_KEEP("num_keep"),
        SEED("seed"),
        NUM_PREDICT("num_predict"),
        TOP_K("top_k"),
        TOP_P("top_p"),
        TFS_Z("tfs_z"),
        TYPICAL_P("typical_p"),
        REPEAT_LAST_N("repeat_last_n"),
        TEMPERATURE("temperature"),
        REPEAT_PENALTY("repeat_penalty"),
        PRESENCE_PENALTY("presence_penalty"),
        FREQUENCY_PENALTY("frequency_penalty"),
        MIROSTAT("mirostat"),
        MIROSTAT_TAU("mirostat_tau"),
        PENALIZE_NEWLINE("penalize_newline"),
        STOP("stop"),
        NUMA("numa"),
        NUM_CTX("num_ctx"),
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
    }

}