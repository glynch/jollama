package com.glynch.jollama.chat;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.glynch.jollama.Format;
import com.glynch.jollama.Options;

public class TestChatRequest {

    @Test
    public void testChatRequest() {
        Message message = Message.user("Why is the sky blue?");
        ChatRequest request = new ChatRequest("llama3", List.of(message), Format.JSON, Options.create(), true, "5s");

        assertAll(
                () -> assertEquals("llama3", request.model()),
                () -> assertEquals(1, request.messages().size()),
                () -> assertEquals(message, request.messages().get(0)),
                () -> assertEquals(Format.JSON, request.format()),
                () -> assertEquals(0, request.options().size()),
                () -> assertEquals(true, request.stream()),
                () -> assertEquals("5s", request.keepAlive()));
    }

}
