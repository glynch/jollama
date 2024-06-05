package com.glynch.ollama.push;

public record PushRequest(String name, Boolean insecure, Boolean stream) {
}
