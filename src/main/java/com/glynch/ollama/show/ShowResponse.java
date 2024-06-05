package com.glynch.ollama.show;

import com.glynch.ollama.Details;

public record ShowResponse(String modelfile, String parameters, String template, Details details) {
}
