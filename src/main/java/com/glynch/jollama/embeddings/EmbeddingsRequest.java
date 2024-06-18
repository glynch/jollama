package com.glynch.jollama.embeddings;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.glynch.jollama.KeepAlive;
import com.glynch.jollama.Options;

/**
 * Embeddings request.
 * 
 * @param model     The name of the model to use.
 * @param prompt    The prompt to use.
 * @param options   The {@link Options options} to use.
 * @param keepAlive The time to keep the model loaded.
 * 
 * @author Graham Lynch
 * 
 * @see Options
 * @see KeepAlive
 */
public record EmbeddingsRequest(
                String model,
                String prompt,
                Options options,
                @JsonProperty("keep_alive") String keepAlive) {
}