package com.glynch.ollama;

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

    @Override
    public String toString() {
        return value;
    }
}
