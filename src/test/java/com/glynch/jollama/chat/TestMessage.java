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

}
