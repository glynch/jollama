package com.glynch.ollama;

import static com.glynch.ollama.ModelName.LLAMA_3;

public enum Model {

    LLAMA_3_LATEST(LLAMA_3, "latest"),
    LLAMA_3_8B(LLAMA_3, "8b"),
    LLAMA_3_70B(LLAMA_3, "70b");
    ;

    private final ModelName modelName;
    private final String tag;

    Model(ModelName modelName, String tag) {
        this.modelName = modelName;
        this.tag = tag;
    }

    public ModelName getModelName() {
        return modelName;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return modelName + ":" + tag;
    }

}
