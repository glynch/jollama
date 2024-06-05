package com.glynch.ollama.push;

public record PushResponse(String status, String digest, Long total) {
}
