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

    public static Message user(String content, List<String> images) {
        return new Message(Role.USER, content, Collections.unmodifiableList(images));
    }

    public static Message user(String content) {
        return user(content, List.of());
    }

    public static Message user(String content, String... images) {
        return user(content, List.of(images));
    }

    public static Message system(String content, List<String> images) {
        return new Message(Role.SYSTEM, content, Collections.unmodifiableList(images));
    }

    public static Message system(String content) {
        return system(content, List.of());
    }

    public static Message system(String content, String... images) {
        return system(content, List.of(images));
    }

    public static Message assistant(String content, List<String> images) {
        return new Message(Role.ASSISTANT, content, images);
    }

    public static Message assistant(String content) {
        return assistant(content, List.of());
    }

    public static Message assistant(String content, String... images) {
        return assistant(content, List.of(images));
    }

    public static Message message(Message message) {
        return new Message(message.role, message.content(), message.images());
    }

}
