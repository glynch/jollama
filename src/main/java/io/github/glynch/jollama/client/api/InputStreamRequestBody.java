package io.github.glynch.jollama.client.api;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class InputStreamRequestBody extends RequestBody {

    private final Path path;
    private final MediaType mediaType;

    public InputStreamRequestBody(Path path, MediaType mediaType) {
        this.path = path;
        this.mediaType = mediaType;
    }

    @Override
    public long contentLength() throws IOException {
        return Files.size(path);
    }

    @Override
    public MediaType contentType() {
        return mediaType;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {

        try (InputStream inputStream = Files.newInputStream(path)) {
            Source source = Okio.source(inputStream);
            sink.writeAll(source);
        }

    }

}
