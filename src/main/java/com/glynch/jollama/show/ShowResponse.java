package com.glynch.jollama.show;

import com.glynch.jollama.Details;

public record ShowResponse(String modelfile, String parameters, String template, String system, Details details,
                String license) {
}
