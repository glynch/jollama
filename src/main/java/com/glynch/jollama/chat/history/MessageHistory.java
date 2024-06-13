package com.glynch.jollama.chat.history;

import java.util.List;

import com.glynch.jollama.chat.Message;

/**
 * Represents a history of messages.
 *
 */
public interface MessageHistory extends Iterable<Message> {

    static MessageHistory create() {
        return new InMemoryMessageHistory();
    }

    void add(MessageHistory history);

    void add(List<Message> messages);

    void add(Message message);

    Message get(int index);

    List<Message> messages();

    void clear();

    int size();

}
