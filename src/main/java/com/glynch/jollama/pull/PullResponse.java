package com.glynch.ollama.pull;

public record PullResponse(String status, String digest, Long total, Long completed) {
}
