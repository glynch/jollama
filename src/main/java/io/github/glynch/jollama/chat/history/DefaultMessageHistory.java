package io.github.glynch.jollama.chat.history;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import io.github.glynch.jollama.chat.Message;
import io.github.glynch.jollama.chat.Role;

public class DefaultMessageHistory implements MessageHistory {

    protected final List<Message> messages = new ArrayList<>();
    private Message system;

    public DefaultMessageHistory() {
    }

    public DefaultMessageHistory(MessageHistory history) {
        add(history.messages());
    }

    public DefaultMessageHistory(List<Message> messages) {
        add(messages);
    }

    @Override
    public void add(MessageHistory history) {
        Objects.requireNonNull(history, "history cannot be null");
        add(history.messages());
    }

    @Override
    public void add(Message message) {
        Objects.requireNonNull(message, "message cannot be null");
        if (message.role() == Role.SYSTEM) {
            this.system = message;
        } else {
            this.messages.add(message);
        }

    }

    @Override
    public void add(List<Message> messages) {
        for (Message message : messages) {
            add(message);
        }
    }

    @Override
    public void clear() {
        this.messages.clear();
        this.system = null;
    }

    @Override
    public Message get(int index) {
        return this.messages.get(index);
    }

    @Override
    public Message remove(int index) {
        return this.messages.remove(index);
    }

    @Override
    public int size() {
        return this.messages.size();
    }

    @Override
    public List<Message> messages() {
        List<Message> messages = new ArrayList<>();
        if (system != null) {
            messages.add(system);
        }
        messages.addAll(this.messages);
        return Collections.unmodifiableList(messages);
    }

    @Override
    public Iterator<Message> iterator() {
        return messages().iterator();
    }

    @Override
    public void system(String message) {
        add(Message.system(message));
    }

    @Override
    public Optional<Message> system() {
        return Optional.ofNullable(system);
    }

}
