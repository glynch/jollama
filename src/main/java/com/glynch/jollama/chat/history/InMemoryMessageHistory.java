package com.glynch.jollama.chat.history;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.glynch.jollama.chat.Message;

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
        this.messages.addAll(history.messages());
    }

    @Override
    public void add(Message message) {
        this.messages.add(message);
    }

    @Override
    public void add(List<Message> messages) {
        this.messages.addAll(messages);
    }

    @Override
    public void clear() {
        this.messages.clear();
    }

    @Override
    public Message get(int index) {
        return this.messages.get(index);
    }

    @Override
    public int size() {
        return this.messages.size();
    }

    @Override
    public List<Message> messages() {
        return Collections.unmodifiableList(messages);
    }

    @Override
    public Iterator<Message> iterator() {
        System.out.println("InMemoryMessageHistory.iterator() " + this.messages.size());
        return this.messages.iterator();
    }

}
