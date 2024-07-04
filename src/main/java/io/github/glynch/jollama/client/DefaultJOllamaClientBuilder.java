package io.github.glynch.jollama.client;

import java.time.Duration;
import java.util.Objects;

import io.github.glynch.jollama.client.JOllamaClient.Builder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;

final class DefaultJOllamaClientBuilder implements JOllamaClient.Builder {

    private static final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    static {
        logging.setLevel(Level.BASIC);
    }

    private final OkHttpClient.Builder builder = new OkHttpClient.Builder();
    private final String host;

    DefaultJOllamaClientBuilder(String host) {
        Objects.requireNonNull(host, "host cannot be null");
        this.host = host;
    }

    @Override
    public Builder log() {
        builder.addInterceptor(logging);
        return this;
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
