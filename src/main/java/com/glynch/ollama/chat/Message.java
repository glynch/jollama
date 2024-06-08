package com.glynch.ollama.chat;

import java.util.List;

public record Message(Role role, String content, List<String> images) {

    public static Message create(String content) {
        return new Message(Role.USER, content, List.of());
    }

    public static Message create(Role role, String content) {
        return new Message(role, content, List.of());
    }

    public static Message create(Role role, String content, List<String> images) {
        return new Message(role, content, images);
    }

    public static Message create(Role role, String content, String... images) {
        return new Message(role, content, List.of(images));
    }

    public static Message user(String content) {
        return create(Role.USER, content);
    }

    public static Message user(String content, List<String> images) {
        return create(Role.USER, content, images);
    }

    public static Message user(String content, String... images) {
        return create(Role.USER, content, images);
    }

    public static Message system(String content) {
        return create(Role.SYSTEM, content);
    }

    public static Message system(String content, List<String> images) {
        return create(Role.USER, content, images);
    }

    public static Message system(String content, String... images) {
        return create(Role.USER, content, images);
    }

    public static Message assistant(String content) {
        return create(Role.ASSISTANT, content);
    }

    public static Message assistant(String content, List<String> images) {
        return create(Role.ASSISTANT, content, images);
    }

    public static Message assistant(String content, String... images) {
        return create(Role.ASSISTANT, content, images);
    }

    public static Message message(Message message) {
        return create(message.role, message.content(), message.images());
    }

}
