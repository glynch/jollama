package com.glynch.jollama.chat;

import java.util.List;

import com.glynch.jollama.Format;
import com.glynch.jollama.Options;

public record ChatRequest(
        String model,
        List<Message> messages,
        Format format,
        Options options,
        Boolean stream,
        String keepAlive) {

}
