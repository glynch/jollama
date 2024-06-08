package com.glynch.ollama.client;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Objects;

import com.glynch.ollama.client.OllamaClient.Builder;

final class DefaultOllamaClientBuilder implements OllamaClient.Builder {

    private final HttpClient.Builder clientBuilder = HttpClient.newBuilder();
    private String host;

    DefaultOllamaClientBuilder() {
        clientBuilder.version(HttpClient.Version.HTTP_2);

    }

    DefaultOllamaClientBuilder(String host) {
        this.host = host;
        clientBuilder.version(HttpClient.Version.HTTP_2);

    }

    @Override
    public Builder followRedirects() {
        clientBuilder.followRedirects(HttpClient.Redirect.NORMAL);
        return this;
    }

    @Override
    public Builder connectTimeout(Duration duration) {
        Objects.requireNonNull(duration, "duration cannot be null");
        clientBuilder.connectTimeout(duration);
        return this;
    }

    @Override
    public OllamaClient build() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'build'");
    }

}
