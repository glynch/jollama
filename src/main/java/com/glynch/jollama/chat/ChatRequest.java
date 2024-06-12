package com.glynch.ollama.chat;

import java.util.ArrayList;
import java.util.List;

import com.glynch.ollama.Format;
import com.glynch.ollama.Options;

public record ChatRequest(
        String model,
        List<Message> messages,
        Format format,
        Options options,
        Boolean stream,
        String keepAlive) {
    public ChatRequest {
        if (messages == null) {
            messages = new ArrayList<>();
        }
    }
}
