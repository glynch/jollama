package com.glynch.jollama.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glynch.jollama.JOllamaError;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import reactor.core.publisher.FluxSink;

public class StreamingResponseCallback<T> implements Callback {

    private final FluxSink<T> sink;
    private final Class<T> type;
    private final ObjectMapper objectMapper;

    public StreamingResponseCallback(FluxSink<T> sink, Class<T> type, ObjectMapper objectMapper) {
        this.sink = sink;
        this.type = type;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onFailure(Call call, IOException exception) {
        sink.error(exception);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

        if (!response.isSuccessful()) {
            if (response.code() == 404) {
                throw new RuntimeException(
                        objectMapper.readValue(response.body().string(),
                                JOllamaError.class).error());
            }
        }

        try (InputStream inputStream = response.body().byteStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;
            T data;

            while ((line = reader.readLine()) != null) {
                data = objectMapper.readValue(line, type);
                sink.next(data);
            }

            sink.complete();
        } catch (IOException e) {
            sink.error(e);
        }

    }

}
