package io.github.glynch.jollama.show;

import io.github.glynch.jollama.Details;

public record ShowResponse(String modelfile, String parameters, String template, String system, Details details,
        String license) {
}
