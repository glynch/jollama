package com.glynch.ollama.chat.history;

import java.util.List;

import com.glynch.ollama.chat.Message;

public interface MessageHistory extends Iterable<Message> {

    void add(MessageHistory history);

    void add(List<Message> messages);

    void add(Message message);

    List<Message> messages();

    void clear();

}
