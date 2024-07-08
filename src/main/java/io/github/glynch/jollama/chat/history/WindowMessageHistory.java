package io.github.glynch.jollama.chat.history;

import java.util.List;
import java.util.Objects;

import io.github.glynch.jollama.chat.Message;
import io.github.glynch.jollama.chat.Role;

public class WindowMessageHistory extends DefaultMessageHistory {

    private final int maxMessages;

    public WindowMessageHistory(int maxMessages) {
        this.maxMessages = maxMessages;
    }

    @Override
    public void add(Message message) {
        Objects.requireNonNull(message, "message cannot be null");
        if (message.role() != Role.SYSTEM) {
            ensureCapacity(this.messages, maxMessages);
        }
        super.add(message);

    }

    private void ensureCapacity(List<Message> messages, int maxMessages) {
        while (messages.size() > maxMessages) {
            this.messages.remove(0);
        }
    }

}
