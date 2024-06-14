package com.glynch.jollama;

import java.util.Arrays;
import java.util.Objects;

public enum Model {

    LLAMA_3_LATEST(Name.LLAMA_3, "latest"),
    LLAMA_3_8B(Name.LLAMA_3, "8b"),
    LLAMA_3_70B(Name.LLAMA_3, "70b"),
    PHI_3_LATEST(Name.PHI_3, "latest"),
    PHI_3_3_8B(Name.PHI_3, "3.8b"),
    PHI_3_14B(Name.PHI_3, "14b"),
    QWEN_2_LATEST(Name.QWEN_2, "latest"),
    QWEN_2_72B(Name.QWEN_2, "72b"),
    QWEN_2_7B(Name.QWEN_2, "7b"),
    QWEN_2_1_5B(Name.QWEN_2, "1.5b"),
    QWEN_2_0_5B(Name.QWEN_2, "0.5b"),
    AYA_LATEST(Name.AYA, "latest"),
    AYA_8B(Name.AYA, "8b"),
    AYA_35B(Name.AYA, "35b"),
    MISTRAL_LATEST(Name.MISTRAL, "latest"),
    MISTRAL_7B(Name.MISTRAL, "7b"),
    GEMMA_LATEST(Name.GEMMA, "latest"),
    GEMMA_2B(Name.GEMMA, "2b"),
    GEMMA_7B(Name.GEMMA, "7b"),
    ORCA_MINI_LATEST(Name.ORCA_MINI, "latest"),
    ORCA_MINI_3B(Name.ORCA_MINI, "3b"),
    ORCA_MINI_7B(Name.ORCA_MINI, "7b"),
    ORCA_MINI_13B(Name.ORCA_MINI, "13b"),
    ORCA_MINI_70B(Name.ORCA_MINI, "70b"),
    NOMIC_EMBED_TEXT_LATEST(Name.NOMIC_EMBED_TEXT, "latest"),
    ;

    private final Name name;
    private final String tag;

    Model(Name name, String tag) {
        this.name = name;
        this.tag = tag;
    }

    public Name getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return name + ":" + tag;
    }

    public static Model of(String name, String tag) {
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(tag, "tag cannot be null");
        return Arrays.stream(values()).filter(v -> v.getName().getName().equals(name) && v.getTag().equals(tag))
                .findFirst()
                .orElse(null);
    }

    public static Model of(String name) {
        return of(name, "latest");
    }

    public static enum Name {

        LLAMA_3("llama3"),
        PHI_3("phi3"),
        AYA("aya"),
        MISTRAL("mistral"),
        GEMMA("gemma"),
        MIXTRAL("mixtral"),
        LLAMA_2("llama2"),
        CODE_GEMMA("codegemma"),
        COMMAND_R("commandr"),
        LLAVA("llava"),
        DBRX("dbrx"),
        QWEN("qwen"),
        CODE_LLAMA("codellama"),
        DOLPHIN_MIXTRAL("dolphin-mixtral"),
        LLAMA_2_UNCENSORED("llama2-uncensored"),
        DEEPSEEK_CODER("deepseek-coder"),
        NOMIC_EMBED_TEXT("nomic-embed-text"),
        MISTRAL_OPEN_ORCA("mistral-openorca"),
        DOLPHIN_MISTRAL("dolphin-mistral"),
        PHI("phi"),
        ORCA_MINI("orca-mini"),
        ZEPHYR("zephyr"),
        NOUS_HERMES("nous-hermes"),
        STARCODE_2("starcode2"),
        DOLPHIN_LLAMA_3("dolphin-llama3"),
        LLAMA_2_CHINESE("llama2-chinese"),
        YI("yi"),
        VICUNA("vicuna"),
        MBXAI_EMBED_LARGE("mbxai-embed-large"),
        WIZARD_VICUNA_UNCENSORED("wizard-vicuna-uncensored"),
        TINY_LLAMA("tinyllama"),
        WIZARD_LM2("wizardlm2"),
        STARCODER("starcoder"),
        OPENCHAT("openchat"),
        OPENHERMES("openhermes"),
        TINY_DOLPHIN("tinydolphin"),
        WIZARDCODER("wizardcoder"),
        STABLE_CODE("stable-code"),
        QWEN_2("qwen2"),
        NEURAL_CHAT("neural-chat"),
        CODESTRAL("codestral"),
        WIZARD_MATH("wizard-math"),
        PHIND_CODELLAMA("phind-codellama"),
        CODE_QWEN("codeqwen"),
        STABLE_LM2("stablelm2"),
        STARLING_LM("starling-lm"),
        DOLPHIN_CODER("dolphincoder"),
        NOUS_HERMES_2("nous-hermes2"),
        ;

        private final String name;

        Name(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }

        public static Name of(String name) {
            return Arrays.stream(values()).filter(v -> v.getName().equals(name)).findFirst()
                    .orElse(null);
        }

    }

}
