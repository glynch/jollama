package io.github.glynch.jollama.push;

public record PushResponse(String status, String digest, Long total) {
}
