package com.glynch.jollama.chat;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Chat message role.
 * 
 * @see Message
 */
public enum Role {

    /**
     * Used for instructions to the model.
     */
    SYSTEM("system"),
    /**
     * Used for user messages.
     */
    USER("user"),
    /**
     * Used for assistant messages. Model response messages.
     */
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

    /**
     * Get the role from the value.
     * 
     * @param value The value.
     * @return The role or null if not found.
     */
    public static Role of(String value) {
        Objects.requireNonNull(value, "value cannot be null");
        return Arrays.stream(values()).filter(v -> v.getValue().equals(value)).findFirst()
                .orElse(null);
    }

}
