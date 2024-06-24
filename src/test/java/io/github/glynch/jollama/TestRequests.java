package io.github.glynch.jollama;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.glynch.jollama.chat.ChatRequest;
import io.github.glynch.jollama.chat.Message;
import io.github.glynch.jollama.copy.CopyRequest;
import io.github.glynch.jollama.create.CreateRequest;
import io.github.glynch.jollama.delete.DeleteRequest;
import io.github.glynch.jollama.generate.GenerateRequest;
import io.github.glynch.jollama.show.ShowRequest;

class TestRequests {

    @Test
    void testCreateRequest() {

        String modelFile = "FROM llaam3\nSYSTEM";
        CreateRequest request = new CreateRequest("glynch/llama3", modelFile, true);

        assertAll(
                () -> assertEquals("glynch/llama3", request.name()),
                () -> assertEquals(modelFile, request.modelfile()),
                () -> assertEquals(true, request.stream()));

    }

    @Test
    void testChatRequest() {
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
    void showRequest() {
        ShowRequest request = new ShowRequest("llama3:latest", false);

        assertAll(
                () -> assertEquals("llama3:latest", request.name()));
    }

    @Test
    void deleteRequest() {
        DeleteRequest request = new DeleteRequest("llama3:latest");

        assertAll(
                () -> assertEquals("llama3:latest", request.name()));
    }

    @Test
    void copyRequest() {
        CopyRequest request = new CopyRequest("llama3:latest", "llama3-copy");

        assertAll(
                () -> assertEquals("llama3:latest", request.source()),
                () -> assertEquals("llama3-copy", request.destination()));
    }

    @Test
    void generateRequest() {
        GenerateRequest request = new GenerateRequest("llama3", "Why is the sky blue?", Collections.emptyList(),
                Format.JSON,
                Options.create(), "", "", Collections.emptyList(), false, null, "5s");
        assertAll(
                () -> assertEquals("llama3", request.model()),
                () -> assertEquals("Why is the sky blue?", request.prompt()),
                () -> assertEquals(0, request.images().size()),
                () -> assertEquals(Format.JSON, request.format()),
                () -> assertEquals(0, request.options().size()),
                () -> assertEquals("", request.system()),
                () -> assertEquals("", request.template()),
                () -> assertEquals(0, request.context().size()),
                () -> assertEquals(false, request.stream()),
                () -> assertEquals(null, request.raw()),
                () -> assertEquals("5s", request.keepAlive()));
    }

}
