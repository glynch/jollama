package com.glynch.ollama;

public enum ModelName {

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

    ModelName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

}
