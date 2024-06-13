package com.glynch.jollama;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.glynch.jollama.Options.Key;

public class TestOptions {

    private Options options;

    @BeforeEach
    public void init() {
        Options.Builder optionsBuilder = Options.builder();
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
                .frequencyPenalty(1.0f)
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

        options = optionsBuilder.build();
    }

    @Test
    public void whenOptionsBuilderPopulatedAllValuesCorrect() {

        assertAll(
                "options",
                () -> assertEquals(5, options.numKeep()),
                () -> assertEquals(42, options.seed()),
                () -> assertEquals(100, options.numPredict()),
                () -> assertEquals(20, options.topK()),
                () -> assertEquals(0.9f, options.topP()),
                () -> assertEquals(0.5f, options.getTfsZ()),
                () -> assertEquals(0.7f, options.typicalP()),
                () -> assertEquals(33, options.repeatLastN()),
                () -> assertEquals(0.8f, options.temperature()),
                () -> assertEquals(1.2f, options.repeatPenalty()),
                () -> assertEquals(1.5f, options.presencePenalty()),
                () -> assertEquals(1.0f, options.frequencyPenalty()),
                () -> assertEquals(1, options.miroStat()),
                () -> assertEquals(0.8f, options.miroStatTau()),
                () -> assertEquals(0.6f, options.miroStatEta()),
                () -> assertTrue(options.penalizeNewline()),
                () -> assertEquals(List.of("\n", "user:"), options.stop()),
                () -> assertFalse(options.numa()),
                () -> assertEquals(1024, options.numCtx()),
                () -> assertEquals(2, options.numBatch()),
                () -> assertEquals(2, options.numGpu()),
                () -> assertEquals(0, options.mainGpu()),
                () -> assertFalse(options.lowVram()),
                () -> assertTrue(options.f16Kv()),
                () -> assertFalse(options.vocabOnly()),
                () -> assertTrue(options.useMmap()),
                () -> assertFalse(options.useMlock()),
                () -> assertEquals(8, options.numThread()));
    }

    @Test
    public void options_Empty_Size_Zero() {
        assertTrue(() -> Options.builder().build().size() == 0);
    }

    @Test
    public void testOptionsFromOptionsBuilder() {

        Options newOptions = new Options.Builder(options).build();
        assertAll(
                "options",
                () -> assertEquals(5, newOptions.numKeep()),
                () -> assertEquals(42, newOptions.seed()),
                () -> assertEquals(100, newOptions.numPredict()),
                () -> assertEquals(20, newOptions.topK()),
                () -> assertEquals(0.9f, newOptions.topP()),
                () -> assertEquals(0.5f, newOptions.getTfsZ()),
                () -> assertEquals(0.7f, newOptions.typicalP()),
                () -> assertEquals(33, newOptions.repeatLastN()),
                () -> assertEquals(0.8f, newOptions.temperature()),
                () -> assertEquals(1.2f, newOptions.repeatPenalty()),
                () -> assertEquals(1.5f, newOptions.presencePenalty()),
                () -> assertEquals(1.0f, newOptions.frequencyPenalty()),
                () -> assertEquals(1, newOptions.miroStat()),
                () -> assertEquals(0.8f, newOptions.miroStatTau()),
                () -> assertEquals(0.6f, newOptions.miroStatEta()),
                () -> assertTrue(newOptions.penalizeNewline()),
                () -> assertEquals(List.of("\n", "user:"), newOptions.stop()),
                () -> assertFalse(options.numa()),
                () -> assertEquals(1024, newOptions.numCtx()),
                () -> assertEquals(2, newOptions.numBatch()),
                () -> assertEquals(2, newOptions.numGpu()),
                () -> assertEquals(0, newOptions.mainGpu()),
                () -> assertFalse(newOptions.lowVram()),
                () -> assertTrue(newOptions.f16Kv()),
                () -> assertFalse(newOptions.vocabOnly()),
                () -> assertTrue(newOptions.useMmap()),
                () -> assertFalse(newOptions.useMlock()),
                () -> assertEquals(8, newOptions.numThread()));
    }

    @Test
    public void testOptionsFromOptionsConstructor() {

        Options newOptions = new Options(options);
        assertAll(
                "options",
                () -> assertEquals(5, newOptions.numKeep()),
                () -> assertEquals(42, newOptions.seed()),
                () -> assertEquals(100, newOptions.numPredict()),
                () -> assertEquals(20, newOptions.topK()),
                () -> assertEquals(0.9f, newOptions.topP()),
                () -> assertEquals(0.5f, newOptions.getTfsZ()),
                () -> assertEquals(0.7f, newOptions.typicalP()),
                () -> assertEquals(33, newOptions.repeatLastN()),
                () -> assertEquals(0.8f, newOptions.temperature()),
                () -> assertEquals(1.2f, newOptions.repeatPenalty()),
                () -> assertEquals(1.5f, newOptions.presencePenalty()),
                () -> assertEquals(1.0f, newOptions.frequencyPenalty()),
                () -> assertEquals(1, newOptions.miroStat()),
                () -> assertEquals(0.8f, newOptions.miroStatTau()),
                () -> assertEquals(0.6f, newOptions.miroStatEta()),
                () -> assertTrue(newOptions.penalizeNewline()),
                () -> assertEquals(List.of("\n", "user:"), newOptions.stop()),
                () -> assertFalse(newOptions.numa()),
                () -> assertEquals(1024, newOptions.numCtx()),
                () -> assertEquals(2, newOptions.numBatch()),
                () -> assertEquals(2, newOptions.numGpu()),
                () -> assertEquals(0, newOptions.mainGpu()),
                () -> assertFalse(newOptions.lowVram()),
                () -> assertTrue(newOptions.f16Kv()),
                () -> assertFalse(newOptions.vocabOnly()),
                () -> assertTrue(newOptions.useMmap()),
                () -> assertFalse(newOptions.useMlock()),
                () -> assertEquals(8, newOptions.numThread()));
    }

    @Test
    public void testOptionsKey() {

        Key key = Key.of("num_keep");
        assertEquals(Key.NUM_KEEP, key);
        assertEquals("num_keep", key.toString());

    }

}
