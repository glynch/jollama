package com.glynch.jollama.chat.history;

import java.util.List;

import com.glynch.jollama.chat.Message;

/**
 * Represents a history of messages.
 * 
 * Can be used to store a history of messages for a chat request.
 * 
 * <pre>
 * MessageHistory history = new InMemoryMessageHistory();
 * client.chat("llama3", "Why is the sky blue?").history(history).stream().subscribe(r -> {
 *     System.out.print(r.message().content());
 * });
 * System.out.println();
 * client.chat("llama3", "How is that different than mie scattering?").history(history)
 *         .stream().subscribe(r -> {
 *             System.out.print(r.message().content());
 *         });
 * System.out.println();
 * </pre>
 * 
 * @author Graham Lynch
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

    Message remove(int index);

    List<Message> messages();

    void clear();

    int size();

}
