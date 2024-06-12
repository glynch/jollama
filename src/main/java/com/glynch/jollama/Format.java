package com.glynch.ollama;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Format {
    JSON("json");

    private final String value;

    Format(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static Format of(String value) {
        Objects.requireNonNull(value, "value cannot be null");
        return Arrays.stream(values()).filter(v -> v.getValue().equals(value)).findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return value;
    }
}
