package com.glynch.jollama.list;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.glynch.jollama.Details;

public record ListModel(
                String name,
                String model,
                @JsonProperty("modified_at") OffsetDateTime modifiedAt,
                long size,
                String digest,
                @JsonProperty("expires_at") OffsetDateTime expiresAt,
                Details details) {
}