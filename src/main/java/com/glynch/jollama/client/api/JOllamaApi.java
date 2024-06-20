package com.glynch.jollama.client.api;

import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import reactor.core.publisher.Flux;

public interface JOllamaApi {

    Response execute(Request request);

    <T> T execute(Request request, Class<T> type);

    <T> T execute(Request request, Class<T> type, Callback callback);

    Response get(String path);

    <T> T get(String path, Class<T> type);

    Response head(String path);

    Response delete(String path, Object body);

    Response post(String path, Object body);

    <T> T post(String path, Object body, Class<T> type);

    <T> Flux<T> stream(String path, Object body, Class<T> type);

}
