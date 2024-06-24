package io.github.glynch.jollama.chat;

import java.util.Collections;
import java.util.List;

/**
 * Chat message.
 * 
 * @param role    The {@link Role role} of the message.
 * @param content The content of the message.
 * @param images  The images of the message. Base64 encoded. Requires multimodel
 *                model
 * 
 * @author Graham Lynch
 * 
 */
public record Message(Role role, String content, List<String> images) {

    /**
     * Create a user message.
     * 
     * @param content The content of the message.
     * @param images  The images of the message. Base64 encoded.
     * @return The message.
     */
    public static Message user(String content, List<String> images) {
        return new Message(Role.USER, content, Collections.unmodifiableList(images));
    }

    /**
     * Create a user message.
     * 
     * @param content The content of the message.
     * @return The message.
     */
    public static Message user(String content) {
        return user(content, List.of());
    }

    /**
     * Create a user message.
     * 
     * @param content The content of the message.
     * @param images  The images of the message. Base64 encoded.
     * @return The message.
     */
    public static Message user(String content, String... images) {
        return user(content, List.of(images));
    }

    /**
     * Create a system message.
     * 
     * @param content The content of the message.
     * @param images  The images of the message. Base64 encoded.
     * @return The message.
     */
    public static Message system(String content, List<String> images) {
        return new Message(Role.SYSTEM, content, Collections.unmodifiableList(images));
    }

    /**
     * Create a system message.
     * 
     * @param content The content of the message.
     * @return The message.
     */
    public static Message system(String content) {
        return system(content, List.of());
    }

    /**
     * Create a system message.
     * 
     * @param content The content of the message.
     * @param images  The images of the message. Base64 encoded.
     * @return The message.
     */
    public static Message system(String content, String... images) {
        return system(content, List.of(images));
    }

    /**
     * Create an assistant message.
     * 
     * @param content The content of the message.
     * @param images  The images of the message. Base64 encoded.
     * @return The message.
     */
    public static Message assistant(String content, List<String> images) {
        return new Message(Role.ASSISTANT, content, images);
    }

    /**
     * Create an assistant message.
     * 
     * @param content The content of the message.
     * @return The message.
     */
    public static Message assistant(String content) {
        return assistant(content, List.of());
    }

    /**
     * Create an assistant message.
     * 
     * @param content The content of the message.
     * @param images  The images of the message. Base64 encoded.
     * @return The message.
     */
    public static Message assistant(String content, String... images) {
        return assistant(content, List.of(images));
    }

}
