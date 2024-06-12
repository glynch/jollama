package com.glynch.ollama;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Details(
        @JsonProperty("parent_model") String parentModel,
        String format,
        String family,
        List<String> families,
        @JsonProperty("parameter_size") String parameterSize,
        @JsonProperty("quantization_level") String quantizationLevel) {
    public Details {
        if (families == null) {
            families = new ArrayList<>();
        }
    }
}
