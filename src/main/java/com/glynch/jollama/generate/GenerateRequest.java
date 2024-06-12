package com.glynch.ollama.generate;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.glynch.ollama.Format;
import com.glynch.ollama.Options;

public record GenerateRequest(
        String model,
        String prompt,
        List<String> images,
        Format format,
        Options options,
        String system,
        String template,
        List<Integer> context,
        Boolean stream,
        Boolean raw,
        @JsonProperty("keep_alive") String keepAlive) {
    public GenerateRequest {
        if (context == null) {
            context = new ArrayList<>();
        }
        if (images == null) {
            images = new ArrayList<>();
        }
    }
}
