package com.glynch.jollama.list;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.glynch.jollama.Details;

public record ListModel(
                String name,
                String model,
                @JsonProperty("modified_at") Instant modifiedAt,
                long size,
                String digest,
                @JsonProperty("expires_at") Instant expiresAt,
                Details details) {
}