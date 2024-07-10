package io.github.glynch.jollama.generate;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Generate response.
 * 
 * @param model              The name of the model used.
 * @param createdAt          When the response was generated.
 * @param response           The response. This will contain the full response
 *                           if not
 *                           streamed. Otherwise each response will contain the
 *                           net
 *                           tokens.
 * @param done               Whether the response is done.
 * @param doneReason         The reason the response is done. For streaming
 *                           response only last message will have this set.
 * @param context            The context. This can be used for subsequent
 *                           requests for
 *                           conversational history.
 * @param totalDuration      The total duration generating the request.
 *                           (nanoseconds)
 * @param loadDuration       The duration loading the model. (nanoseconds)
 * @param promptEvalDuration The duration evaluating the prompt. (nanoseconds)
 * @param evalCount          The number of tokens in the response.
 * @param evalDuration       The duration generating the response. (nanoseconds)
 * 
 * @see <a href=
 *      "https://github.com/ollama/ollama/blob/main/docs/api.md#generate-a-completion">Generate
 *      a completion</a>
 */
public record GenerateResponse(
                String model,
                @JsonProperty("created_at") Instant createdAt,
                String response,
                boolean done,
                @JsonProperty("done_reason") String doneReason,
                List<Integer> context,
                @JsonProperty("total_duration") Long totalDuration,
                @JsonProperty("load_duration") Long loadDuration,
                @JsonProperty("prompt_eval_count") Long promptEvalCount,
                @JsonProperty("prompt_eval_duration") Long promptEvalDuration,
                @JsonProperty("eval_count") Long evalCount,
                @JsonProperty("eval_duration") Long evalDuration) {

}
