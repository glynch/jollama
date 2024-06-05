package com.glynch.ollama.chat;

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

}
