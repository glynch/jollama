package com.glynch.ollama.embeddings;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.glynch.ollama.Options;

public record EmbeddingsRequest(
                String model,
                String prompt,
                Options options,
                @JsonProperty("keep_alive") String keepAlive) {
}