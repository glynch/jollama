package io.github.glynch.jollama.chat.history;

import java.util.List;
import java.util.Optional;

import io.github.glynch.jollama.chat.Message;

/**
 * Represents a history of messages.
 * 
 * Can be used to store a history of messages for a chat request.
 * 
 * <pre>
 * MessageHistory history = MemoryHistory.create();
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
        return new DefaultMessageHistory();
    }

    /**
     * Add a system message
     * 
     * @param message the message
     */
    void system(String message);

    Optional<Message> system();

    /**
     * Add a {@link MessageHistory} to this history.
     * 
     * @param history The {@code MessageHistory} to add.
     */
    void add(MessageHistory history);

    /**
     * Add a list of {@link Message messages} to this history.
     * 
     * @param messages The list of messages to add.
     */
    void add(List<Message> messages);

    /**
     * Add a {@link Message} to this history.
     * 
     * @param message The message to add.
     */
    void add(Message message);

    Message get(int index);

    /**
     * Remove a message from the history at the specified index.
     * 
     * @param index The index of the message to remove.
     * @return The removed message.
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    Message remove(int index) throws IndexOutOfBoundsException;

    /**
     * Get an immutable list of {@link Message} messages in the history.
     * 
     * @return The messages in the history.
     */
    List<Message> messages();

    /**
     * Clear the history.
     */
    void clear();

    /**
     * Get the size of the history.
     * 
     * @return The size of the history.
     */
    int size();

}
