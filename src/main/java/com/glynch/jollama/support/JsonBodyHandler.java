package com.glynch.jollama.support;

import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodySubscriber;
import java.net.http.HttpResponse.BodySubscribers;
import java.net.http.HttpResponse.ResponseInfo;
import java.nio.charset.StandardCharsets;

import com.glynch.jollama.support.Body.Mappers;

public class JsonBodyHandler<T> implements BodyHandler<T> {

    private final Class<T> type;

    public JsonBodyHandler(Class<T> type) {
        this.type = type;
    }

    @Override
    public BodySubscriber<T> apply(ResponseInfo responseInfo) {

        BodySubscriber<String> upstream = BodySubscribers.ofString(StandardCharsets.UTF_8);
        int statusCode = responseInfo.statusCode();
        // Should check for 400, 500 ...
        if (responseInfo.statusCode() == 404) {
            return BodySubscribers.mapping(
                    upstream,
                    Mappers.exceptionally(responseInfo));
        }

        return BodySubscribers.mapping(
                upstream,
                Mappers.map(type));
    }

}
