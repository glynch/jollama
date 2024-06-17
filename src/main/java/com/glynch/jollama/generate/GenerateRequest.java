package com.glynch.jollama.generate;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.glynch.jollama.Format;
import com.glynch.jollama.Options;

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

}
