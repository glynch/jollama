package com.glynch.ollama.push;

public final class PushRequest {

    private final String name;
    private final Boolean insecure;
    private final Boolean stream;

    public PushRequest(String name, Boolean insecure, Boolean stream) {
        this.name = name;
        this.insecure = insecure;
        this.stream = stream;
    }

    public String getName() {
        return name;
    }

    public Boolean isInsecure() {
        return insecure;
    }

    public Boolean isStream() {
        return stream;
    }

}
