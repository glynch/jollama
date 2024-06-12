package com.glynch.jollama.pull;

public record PullResponse(String status, String digest, Long total, Long completed) {
}
