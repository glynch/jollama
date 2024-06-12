package com.glynch.jollama.chat;

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
        Message message = Message.assistant("Why is the sky blue?");

        assertAll(
                () -> assertEquals("Why is the sky blue?", message.content()),
                () -> assertEquals(Role.ASSISTANT, message.role()),
                () -> assertEquals(0, message.images().size()));
    }

    @Test
    public void testSystemtMessage() {
        Message message = Message.system("Why is the sky blue?");

        assertAll(
                () -> assertEquals("Why is the sky blue?", message.content()),
                () -> assertEquals(Role.SYSTEM, message.role()),
                () -> assertEquals(0, message.images().size()));
    }

    @Test
    public void testMessageFromMessage() {
        Message message = Message.user("Why is the sky blue?");
        Message message2 = Message.message(message);

        assertAll(
                () -> assertEquals("Why is the sky blue?", message2.content()),
                () -> assertEquals(Role.USER, message2.role()),
                () -> assertEquals(0, message2.images().size()));
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
    public void testSystemMessageNoImages() {
        Message message = Message.system("Why is the sky blue?", new String[0]);

        assertAll(
                () -> assertEquals("Why is the sky blue?", message.content()),
                () -> assertEquals(Role.SYSTEM, message.role()),
                () -> assertEquals(0, message.images().size()));
    }

    @Test
    public void testAssitantMessageNoImages() {
        Message message = Message.assistant("Why is the sky blue?", new String[0]);

        assertAll(
                () -> assertEquals("Why is the sky blue?", message.content()),
                () -> assertEquals(Role.ASSISTANT, message.role()),
                () -> assertEquals(0, message.images().size()));
    }

}
