package com.glynch.jollama.embeddings;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.glynch.jollama.Options;

public record EmbeddingsRequest(
        String model,
        String prompt,
        Options options,
        @JsonProperty("keep_alive") String keepAlive) {
}