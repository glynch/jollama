package io.github.glynch.jollama.show;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.glynch.jollama.Details;

/**
 * Show response.
 * 
 * @param modelfile  The model file.
 * @param parameters The parameters.
 * @param template   The template.
 * @param system     The system.
 * @param details    The {@link Details}.
 * @param license    The license.
 * @param modelInfo  The {@link ModelInfo}.
 * 
 */
public record ShowResponse(String modelfile, String parameters, String template, String system, Details details,
                String license, @JsonProperty("model_info") ModelInfo modelInfo) {
}
