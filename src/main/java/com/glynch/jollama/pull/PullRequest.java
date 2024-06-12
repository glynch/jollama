package com.glynch.ollama.pull;

public record PullRequest(String name, Boolean insecure, Boolean stream) {
}
