package com.glynch.ollama.chat;

import java.util.List;

public interface MessageHistory extends Iterable<Message> {

    void add(MessageHistory history);

    void add(List<Message> messages);

    void add(Message message);

    List<Message> messages();

    void clear();

}
