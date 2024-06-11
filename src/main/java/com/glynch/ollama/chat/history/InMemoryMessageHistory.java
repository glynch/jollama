package com.glynch.ollama.chat.history;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.glynch.ollama.chat.Message;

public class InMemoryMessageHistory implements MessageHistory {

    List<Message> messages = new ArrayList<>();

    public InMemoryMessageHistory() {
    }

    public InMemoryMessageHistory(MessageHistory history) {
        add(history);
    }

    public InMemoryMessageHistory(List<Message> messages) {
        add(messages);
    }

    @Override
    public void add(MessageHistory history) {
        messages.addAll(history.messages());
    }

    @Override
    public void add(Message message) {
        messages.add(message);
    }

    @Override
    public void add(List<Message> messages) {
        this.messages.addAll(messages);
    }

    @Override
    public void clear() {
        messages.clear();
    }

    @Override
    public List<Message> messages() {
        return Collections.unmodifiableList(messages);
    }

    @Override
    public Iterator<Message> iterator() {
        return messages.iterator();
    }

}
