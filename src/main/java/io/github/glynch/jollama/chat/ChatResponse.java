package io.github.glynch.jollama.chat;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Chat response.
 * 
 * @param model              The model used for the response.
 * @param createdAt          When the response was generated.
 * @param message            The response {@link Message} with
 *                           {@link Role#ASSISTANT} and
 *                           content.
 * @param doneReason         The reason the response is done. Populated when
 *                           {@code done} is {@code true}. For streaming
 *                           response only last message will have this set.
 * @param done               Whether the response is done.
 * @param totalDuration      The total duration of the response. (nanoseconds)
 * @param loadDuration       The duration of loading the model. (nanoseconds)
 * @param promptEvalCount    The number of tokens in the prompt.
 * @param promptEvalDuration The duration of evaluating the prompt.
 *                           (nanoseconds)
 * @param evalCount          The number of tokens in the response.
 * @param evalDuration       The time generating the response. (nanoseconds)
 * 
 * @author Graham Lynch
 * 
 */
public record ChatResponse(
        String model,
        @JsonIgnore @JsonProperty("created_at") Instant createdAt,
        Message message,
        @JsonProperty("done_reason") String doneReason,
        boolean done,
        @JsonProperty("total_duration") Long totalDuration,
        @JsonProperty("load_duration") Long loadDuration,
        @JsonProperty("prompt_eval_count") Long promptEvalCount,
        @JsonProperty("prompt_eval_duration") Long promptEvalDuration,
        @JsonProperty("eval_count") Long evalCount,
        @JsonProperty("eval_duration") Long evalDuration) {
}