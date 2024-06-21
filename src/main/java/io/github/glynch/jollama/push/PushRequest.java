package io.github.glynch.jollama.push;

public record PushRequest(String name, Boolean insecure, Boolean stream) {
}
