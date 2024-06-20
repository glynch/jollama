package com.glynch.jollama.list;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.glynch.jollama.Details;

/**
 * List model.
 * 
 * @param name       The name of the model.
 * @param model      The model.
 * @param modifiedAt When the model was last modified.
 * @param size       The size of the model. (bytes)
 * @param digest     The sha256 digest of the model.
 * @param expiresAt  When the model will expire.
 * @param details    The {@link Details details} of the model.
 */
public record ListModel(
        String name,
        String model,
        @JsonProperty("modified_at") Instant modifiedAt,
        long size,
        String digest,
        @JsonProperty("expires_at") Instant expiresAt,
        Details details) {
}