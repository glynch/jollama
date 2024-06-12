package com.glynch.jollama.client;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Objects;

import com.glynch.jollama.client.JOllamaClient.Builder;

final class DefaultJOllamaClientBuilder implements JOllamaClient.Builder {

    private final HttpClient.Builder clientBuilder = HttpClient.newBuilder();
    private String host;

    DefaultJOllamaClientBuilder(String host) {
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
    public JOllamaClient build() {
        return new DefaultJOllamaClient(clientBuilder.build(), host);
    }

}
