package com.glynch.jollama;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model details
 * 
 * @param parentModel       The parent model.
 * @param format            The format. i.e gguf
 * @param family            The family. i.e llama
 * @param families          The families.
 * @param parameterSize     The parameter size. i.e 8B
 * @param quantizationLevel The quantization level. i.e Q4_K_M
 * 
 */
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
