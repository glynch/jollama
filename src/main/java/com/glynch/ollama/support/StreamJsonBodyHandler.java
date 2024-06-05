package com.glynch.ollama.support;

import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodySubscriber;
import java.net.http.HttpResponse.BodySubscribers;
import java.net.http.HttpResponse.ResponseInfo;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public class StreamJsonBodyHandler<T> implements BodyHandler<Stream<T>> {

    private final Class<T> type;

    public StreamJsonBodyHandler(Class<T> type) {
        this.type = type;
    }

    @Override
    public BodySubscriber<Stream<T>> apply(ResponseInfo responseInfo) {
        BodySubscriber<Stream<String>> upstream = BodySubscribers.ofLines(StandardCharsets.UTF_8);
        int statusCode = responseInfo.statusCode();

        if (statusCode == 404 || statusCode == 400 || statusCode == 500) {
            return BodySubscribers.mapping(
                    upstream,
                    (lines) -> {
                        return lines.map(Body.Mappers.exceptionally(type));
                    });
        }

        return BodySubscribers.mapping(
                upstream,
                (lines) -> {
                    return lines.map(
                            Body.Mappers.map(type));
                });
    }

}