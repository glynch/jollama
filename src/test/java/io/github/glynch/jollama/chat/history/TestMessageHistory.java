package io.github.glynch.jollama.chat.history;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.glynch.jollama.chat.Message;

public class TestMessageHistory {

    private MessageHistory history;

    @BeforeEach
    public void init() {
        history = new DefaultMessageHistory();
    }

    @Test
    public void testMessageHistoryAddOneMessage() {
        Message message = Message.user("Why is the sky blue?");
        history.add(message);

        assertAll(
                () -> assertEquals(1, history.size()),
                () -> assertEquals(message, history.get(0)));

    }

    @Test
    public void testMessageHistoryAddMessageHistory() {
        Message message = Message.user("Why is the sky blue?");
        history.add(message);

        MessageHistory history2 = new DefaultMessageHistory();
        history2.add(history);

        assertAll(
                () -> assertEquals(1, history2.size()),
                () -> assertEquals(message, history2.get(0)));

    }

    @Test
    public void testMessageHistoryAddListOfMessages() {
        Message user = Message.user("Why is the sky blue?");
        Message assistant = Message.assistant("Because of Rayleigh scattering.");
        history.add(List.of(user, assistant));

        assertAll(
                () -> assertEquals(2, history.size()),
                () -> assertEquals(user, history.get(0)),
                () -> assertEquals(assistant, history.get(1)));

    }

    @Test
    public void testMessageHistoryClear() {
        Message user = Message.user("Why is the sky blue?");
        Message assistant = Message.assistant("Because of Rayleigh scattering.");
        history.add(List.of(user, assistant));

        history.clear();

        assertAll(
                () -> assertEquals(0, history.size()));

    }

    @Test
    public void testMessageHistoryConstructWithHistory() {
        Message message = Message.user("Why is the sky blue?");
        history.add(message);

        MessageHistory history2 = new DefaultMessageHistory(history);

        assertAll(
                () -> assertEquals(1, history2.size()),
                () -> assertEquals(message, history2.get(0)));

    }

    @Test
    public void testMessageHistoryConstructWithList() {
        Message message = Message.user("Why is the sky blue?");
        history.add(message);

        MessageHistory history2 = new DefaultMessageHistory(history.messages());

        assertAll(
                () -> assertEquals(1, history2.size()),
                () -> assertEquals(message, history2.get(0)));

    }

    @Test
    public void testMessageHistoryIterator() {
        Message user = Message.user("Why is the sky blue?");
        Message assistant = Message.assistant("Because of Rayleigh scattering.");
        history.add(List.of(user, assistant));

        int i = 0;
        for (Message message : history) {
            assertEquals(history.get(i++), message);
        }
    }

    @Test
    public void testMessageHistoryCreate() {
        MessageHistory history = MessageHistory.create();
        assertEquals(0, history.size());
    }

    @Test
    public void testMessageHistoryRemoveMessage() {
        Message user = Message.user("Why is the sky blue?");
        Message assistant = Message.assistant("Because of Rayleigh scattering.");
        history.add(List.of(user, assistant));

        history.remove(0);

        assertAll(
                () -> assertEquals(1, history.size()),
                () -> assertEquals(assistant, history.get(0)));

    }

}
