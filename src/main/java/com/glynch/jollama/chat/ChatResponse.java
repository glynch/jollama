package com.glynch.jollama.chat;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ChatResponse(
        String model,
        @JsonIgnore @JsonProperty("created_at") Instant createdAt,
        Message message,
        @JsonProperty("done_reason") String doneReason,
        boolean done,
        @JsonProperty("total_duration") long totalDuration,
        @JsonProperty("load_duration") long loadDuration,
        @JsonProperty("prompt_eval_duration") long promptEvalDuration,
        @JsonProperty("eval_count") long evalCount,
        @JsonProperty("eval_duration") long evalDuration) {
}