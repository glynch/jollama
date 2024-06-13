package com.glynch.jollama.generate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GenerateResponse(
        String model,
        @JsonProperty("created_at") Instant createdAt,
        String response,
        boolean done,
        @JsonProperty("done_reason") String doneReason,
        List<Integer> context,
        @JsonProperty("total_duration") Long totalDuration,
        @JsonProperty("load_duration") Long loadDuration,
        @JsonProperty("prompt_eval_duration") Long promptEvalDuration,
        @JsonProperty("eval_count") Long evalCount,
        @JsonProperty("eval_duration") Long evalDuration) {
    public GenerateResponse {
        if (context == null) {
            context = new ArrayList<>();
        }
    }
}
