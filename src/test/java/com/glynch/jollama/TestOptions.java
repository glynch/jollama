package com.glynch.ollama;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestOptions {

    private Options.Builder optionsBuilder;

    @BeforeEach
    public void init() {
        optionsBuilder = new Options.Builder();
    }

    @Test
    public void whenOptionsBuilderPopulatedAllValuesCorrect() {
        optionsBuilder
                .numKeep(5)
                .seed(42)
                .numPredict(100)
                .topK(20)
                .topP(0.9f)
                .tfsZ(0.5f)
                .typicalP(0.7f)
                .repeatLastN(33)
                .temperature(0.8f)
                .repeatPenalty(1.2f)
                .presencePenalty(1.5f)
                .miroStat(1)
                .miroStatTau(0.8f)
                .miroStatEta(0.6f)
                .penalizeNewline(true)
                .stop(List.of("\n", "user:"))
                .numa(false)
                .numCtx(1024)
                .numBatch(2)
                .numGpu(2)
                .mainGpu(0)
                .lowVram(false)
                .f16Kv(true)
                .vocabOnly(false)
                .useMmap(true)
                .useMlock(false)
                .numThread(8);

        Options options = optionsBuilder.build();
        assertAll(
                "options",
                () -> assertEquals(5, options.getNumKeep()),
                () -> assertEquals(42, options.getSeed()),
                () -> assertEquals(100, options.getNumPredict()),
                () -> assertEquals(20, options.getTopK()),
                () -> assertEquals(0.9f, options.getTopP()),
                () -> assertEquals(0.5f, options.getTfsZ()),
                () -> assertEquals(0.7f, options.getTypicalP()),
                () -> assertEquals(33, options.getRepeatLastN()),
                () -> assertEquals(0.8f, options.getTemperature()),
                () -> assertEquals(1.2f, options.getRepeatPenalty()),
                () -> assertEquals(1.5f, options.getPresencePenalty()),
                () -> assertEquals(1, options.getMiroStat()),
                () -> assertEquals(0.8f, options.getMiroStatTau()),
                () -> assertEquals(0.6f, options.getMiroStatEta()),
                () -> assertTrue(options.getPenalizeNewline()),
                () -> assertEquals(List.of("\n", "user:"), options.getStop()),
                () -> assertFalse(options.getNuma()),
                () -> assertEquals(1024, options.getNumCtx()),
                () -> assertEquals(2, options.getNumBatch()),
                () -> assertEquals(2, options.getNumGpu()),
                () -> assertEquals(0, options.getMainGpu()),
                () -> assertFalse(options.getLowVram()),
                () -> assertTrue(options.getF16Kv()),
                () -> assertFalse(options.getVocabOnly()),
                () -> assertTrue(options.getUseMmap()),
                () -> assertFalse(options.getUseMlock()),
                () -> assertEquals(8, options.getNumThread()));
    }

    @Test
    public void whenOptionsEmptySizeIsZero() {
        Options options = optionsBuilder.build();
        assertTrue(() -> options.size() == 0);
    }

    @Test
    public void whenOptionsPutExceptionThrown() {
        Options options = optionsBuilder.build();
        assertThrows(UnsupportedOperationException.class, () -> {
            options.put("dummy", "dummy");
        });

    }

}
