package com.glynch.jollama.support;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.glynch.jollama.Details;
import com.glynch.jollama.chat.ChatResponse;
import com.glynch.jollama.chat.Role;
import com.glynch.jollama.create.CreateResponse;
import com.glynch.jollama.generate.GenerateResponse;
import com.glynch.jollama.list.ListModels;

public class TestBodyMapper {

    @Test
    public void testBodyMapperGenerateResponse() {

        GenerateResponse response = Body.Mappers.map(GenerateResponse.class).apply(
                "{\"model\":\"llama3\",\"created_at\":\"2024-06-12T07:41:44.589776Z\",\"response\":\"Ts\",\"done\":false}");

        assertAll(
                () -> assertEquals("llama3", response.model()),
                () -> assertEquals("2024-06-12T07:41:44.589776Z", response.createdAt().toString()),
                () -> assertEquals("Ts", response.response()),
                () -> assertEquals(false, response.done())

        );

    }

    @Test
    public void testBodyMapperChatResponse() throws IOException {

        String json = Files.readString(Path.of("src/test/resources/chat-response.json"));
        ChatResponse response = Body.Mappers.map(ChatResponse.class).apply(json);

        assertAll(
                () -> assertEquals("llama3", response.model()),
                () -> assertEquals("2024-06-12T07:47:29.838719Z", response.createdAt().toString()),
                () -> assertEquals(Role.ASSISTANT, response.message().role()),
                () -> assertEquals("What a great question", response.message().content()),
                () -> assertEquals(true, response.done()),
                () -> assertEquals(7032225625L, response.totalDuration()),
                () -> assertEquals(1066583L, response.loadDuration()),
                () -> assertEquals(149474000L, response.promptEvalDuration()),
                () -> assertEquals(378L, response.evalCount()),
                () -> assertEquals(6880173000L, response.evalDuration()),
                () -> assertEquals("stop", response.doneReason())

        );

    }

    @Test
    public void testBodyMapperCreateResponse() {
        String json = "{\"status\":\"success\"}";
        CreateResponse response = Body.Mappers.map(CreateResponse.class).apply(json);
        assertEquals("success", response.status());
    }

    @Test
    public void testBodyMapperListModels() throws IOException {
        String json = Files.readString(Path.of("src/test/resources/list-response.json"));
        ListModels response = Body.Mappers.map(ListModels.class).apply(json);
        Details llama3Details = new Details("", "gguf", "llama", List.of("llama"), "8.0B", "Q4_0");
        Details phi3Details = new Details("", "gguf", "phi3", List.of("phi3"), "3.8B", "Q4_K_M");

        assertAll(
                () -> assertEquals(2, response.models().size()),
                () -> assertEquals("mario-test:latest", response.models().get(0).model()),
                () -> assertEquals("mario-test:latest", response.models().get(0).name()),
                () -> assertEquals("phi3:latest", response.models().get(1).name()),
                () -> assertEquals("phi3:latest", response.models().get(1).model()),
                // () -> assertEquals("2024-06-12T16:04:49.485805858+07:00",
                // response.models().get(0).modifiedAt().toString()),
                // () -> assertEquals("2024-06-06T00:05:13.694332263+07:00",
                // response.models().get(1).modifiedAt().toString()),
                () -> assertEquals(4661224708L, response.models().get(0).size()),
                () -> assertEquals(2393232963L, response.models().get(1).size()),
                () -> assertEquals(llama3Details, response.models().get(0).details()),
                () -> assertEquals(phi3Details, response.models().get(1).details()));

    }

}
