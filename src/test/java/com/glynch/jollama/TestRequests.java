package com.glynch.jollama;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.glynch.jollama.chat.ChatRequest;
import com.glynch.jollama.chat.Message;
import com.glynch.jollama.copy.CopyRequest;
import com.glynch.jollama.create.CreateRequest;
import com.glynch.jollama.delete.DeleteRequest;
import com.glynch.jollama.show.ShowRequest;

public class TestRequests {

    @Test
    public void testCreateRequest() {

        String modelFile = "FROM llaam3\nSYSTEM";
        CreateRequest request = new CreateRequest("glynch/llama3", modelFile, true, null);

        assertAll(
                () -> assertEquals("glynch/llama3", request.name()),
                () -> assertEquals(modelFile, request.modelfile()),
                () -> assertEquals(true, request.stream()),
                () -> assertEquals(null, request.path()));

    }

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

    @Test
    public void showRequest() {
        ShowRequest request = new ShowRequest("llama3:latest");

        assertAll(
                () -> assertEquals("llama3:latest", request.name()));
    }

    @Test
    public void deleteRequest() {
        DeleteRequest request = new DeleteRequest("llama3:latest");

        assertAll(
                () -> assertEquals("llama3:latest", request.name()));
    }

    @Test
    public void copyRequest() {
        CopyRequest request = new CopyRequest("llama3:latest", "llama3-copy");

        assertAll(
                () -> assertEquals("llama3:latest", request.source()),
                () -> assertEquals("llama3-copy", request.destination()));
    }

}
