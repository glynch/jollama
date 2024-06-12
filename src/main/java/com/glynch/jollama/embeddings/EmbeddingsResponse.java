package com.glynch.jollama.embeddings;

import java.util.ArrayList;
import java.util.List;

public record EmbeddingsResponse(List<Double> embedding) {
    public EmbeddingsResponse {
        if (embedding == null) {
            embedding = new ArrayList<>();
        }
    }
}