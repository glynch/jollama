package com.glynch.jollama.push;

public record PushRequest(String name, Boolean insecure, Boolean stream) {
}
