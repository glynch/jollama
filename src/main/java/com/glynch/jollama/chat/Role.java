package com.glynch.jollama.chat;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {

    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant");

    public final String value;

    private Role(String value) {
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

    public static Role of(String value) {
        Objects.requireNonNull(value, "value cannot be null");
        return Arrays.stream(values()).filter(v -> v.getValue().equals(value)).findFirst()
                .orElse(null);
    }

}
