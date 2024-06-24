package io.github.glynch.jollama.chat;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestMessage {

    @Test
    public void testUserMessage() {
        Message message = Message.user("Why is the sky blue?");

        assertAll(
                () -> assertEquals("Why is the sky blue?", message.content()),
                () -> assertEquals(Role.USER, message.role()),
                () -> assertEquals(0, message.images().size()));
    }

    @Test
    public void testAssistantMessage() {
        Message message = Message.assistant("Because of Raleigh scattering.");

        assertAll(
                () -> assertEquals("Because of Raleigh scattering.", message.content()),
                () -> assertEquals(Role.ASSISTANT, message.role()),
                () -> assertEquals(0, message.images().size()));
    }

    @Test
    public void testSystemMessage() {
        Message message = Message.system("You are an expert meteorologist.");

        assertAll(
                () -> assertEquals("You are an expert meteorologist.", message.content()),
                () -> assertEquals(Role.SYSTEM, message.role()),
                () -> assertEquals(0, message.images().size()));
    }

    @Test
    public void testUserMessageNoImages() {
        Message message = Message.user("Why is the sky blue?", new String[0]);

        assertAll(
                () -> assertEquals("Why is the sky blue?", message.content()),
                () -> assertEquals(Role.USER, message.role()),
                () -> assertEquals(0, message.images().size()));
    }

    @Test
    public void testAssistantMessageNoImages() {
        Message message = Message.assistant("Because of Raleigh scattering.", new String[0]);

        assertAll(
                () -> assertEquals("Because of Raleigh scattering.", message.content()),
                () -> assertEquals(Role.ASSISTANT, message.role()),
                () -> assertEquals(0, message.images().size()));
    }

    @Test
    public void testSystemMessageNoImages() {
        Message message = Message.system("You are an expert meteorologist.", new String[0]);

        assertAll(
                () -> assertEquals("You are an expert meteorologist.", message.content()),
                () -> assertEquals(Role.SYSTEM, message.role()),
                () -> assertEquals(0, message.images().size()));
    }

}
