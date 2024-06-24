package io.github.glynch.jollama.client.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.github.glynch.jollama.client.JOllamaClientException;
import io.github.glynch.jollama.client.JOllamaClientRequestException;
import io.github.glynch.jollama.support.DefaultResponseStatusErrorHandler;
import io.github.glynch.jollama.support.ResponseStatusErrorHandler;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import reactor.core.publisher.Flux;

public class DefaultJOllamaApi implements JOllamaApi {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final JavaTimeModule javaTimeModule = new JavaTimeModule();

    static {
        objectMapper.registerModule(javaTimeModule);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }
    private static final MediaType APPLICATION_JSON = MediaType.parse("application/json");
    private static final ResponseStatusErrorHandler errorHandler = new DefaultResponseStatusErrorHandler(objectMapper);

    private final OkHttpClient client;
    private final String host;

    public DefaultJOllamaApi(OkHttpClient client, String host) {
        this.client = client;
        this.host = host;
    }

    private HttpUrl getUrl(String path) {
        return HttpUrl.parse(this.host + path);
    }

    @Override
    public String host() {
        return host;
    }

    @Override
    public int connectTimeout() {
        return client.connectTimeoutMillis();
    }

    @Override
    public int readTimeout() {
        return client.readTimeoutMillis();
    }

    @Override
    public boolean followRedirects() {
        return client.followRedirects();
    }

    @Override
    public boolean followSslRedirects() {
        return client.followSslRedirects();
    }

    @Override
    public Response execute(Request request) {
        try {
            return client.newCall(request).execute();
        } catch (Exception e) {
            throw new JOllamaClientRequestException(e.getMessage(), request.url().toString(), request.method());
        }
    }

    @Override
    public <T> T execute(Request request, Class<T> type) {
        T data = null;
        try {
            Response response = execute(request);
            if (response.isSuccessful()) {
                data = objectMapper.readValue(response.body().string(), type);
            } else {
                errorHandler.handleError(response);
            }
        } catch (IOException e) {
            throw new JOllamaClientRequestException(e.getMessage(), request.url().toString(), request.method());
        }
        return data;
    }

    @Override
    public <T> T execute(Request request, Class<T> type, Callback callback) {
        try {
            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            throw new JOllamaClientException(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Response get(String path) {
        Request request = new Request.Builder()
                .url(getUrl(path))
                .header("Accept", "application/json")
                .get()
                .build();
        return execute(request);
    }

    @Override
    public <T> T get(String path, Class<T> type) {
        Request request = new Request.Builder()
                .url(getUrl(path))
                .header("Accept", "application/json")
                .get()
                .build();
        return execute(request, type);
    }

    @Override
    public Response head(String path) {
        Request request = new Request.Builder()
                .url(getUrl(path))
                .head()
                .build();
        return execute(request);
    }

    @Override
    public Response delete(String path, Object body) {
        Request request = new Request.Builder()
                .url(getUrl(path))
                .header("Content-type", "application/json")
                .delete(json(body))
                .build();
        return execute(request);
    }

    @Override
    public Response post(String path, Object body) {
        Request request = new Request.Builder()
                .url(getUrl(path))
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .post(json(body))
                .build();
        return execute(request);
    }

    @Override
    public <T> T post(String path, Object body, Class<T> type) {
        Request request = new Request.Builder()
                .url(getUrl(path))
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .post(json(body))
                .build();

        return execute(request, type);
    }

    @Override
    public Response upload(String path, Path filePath) {

        InputStreamRequestBody requestBody = new InputStreamRequestBody(filePath,
                MediaType.parse("application/octet-stream"));

        Request request = new Request.Builder()
                .url(getUrl(path))
                .post(requestBody)
                .build();

        return execute(request);
    }

    @Override
    public <T> Flux<T> stream(String path, Object body, Class<T> type) {
        Request request = new Request.Builder()
                .url(getUrl(path))
                .header("Content-type", "application/json")
                .header("Accept", "application/json")
                .header("Accept", "application/ndjson")
                .post(json(body))
                .build();

        Response response = execute(request);

        if (!response.isSuccessful()) {
            errorHandler.handleError(response);
        }

        return Flux.create(
                sink -> {
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
                });

    }

    private static RequestBody json(Object body) {
        RequestBody requestBody = null;
        try {
            requestBody = RequestBody.create(objectMapper.writeValueAsString(body),
                    APPLICATION_JSON);
        } catch (JsonProcessingException e) {

        }
        return requestBody;
    }

}
