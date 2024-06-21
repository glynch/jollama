package io.github.glynch.jollama.client;

import java.time.Duration;
import java.util.Objects;

import io.github.glynch.jollama.client.JOllamaClient.Builder;
import okhttp3.OkHttpClient;

final class DefaultJOllamaClientBuilder implements JOllamaClient.Builder {

    private final OkHttpClient.Builder builder = new OkHttpClient.Builder();
    private String host;

    DefaultJOllamaClientBuilder(String host) {
        this.host = host;

    }

    @Override
    public Builder followRedirects() {
        builder.followRedirects(true);
        builder.followSslRedirects(false);

        return this;
    }

    @Override
    public Builder followRedirectsNever() {
        builder.followRedirects(false);
        builder.followSslRedirects(false);
        return this;
    }

    @Override
    public Builder followRedirectsAlways() {
        builder.followSslRedirects(true);
        builder.followSslRedirects(true);
        return this;
    }

    @Override
    public Builder connectTimeout(Duration duration) {
        Objects.requireNonNull(duration, "duration cannot be null");
        builder.connectTimeout(duration);
        return this;
    }

    @Override
    public Builder readTimeout(Duration duration) {
        Objects.requireNonNull(duration, "duration cannot be null");
        builder.readTimeout(duration);
        return this;
    }

    @Override
    public JOllamaClient build() {
        return new DefaultJOllamaClient(builder.build(), host);
    }

}
