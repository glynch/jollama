package com.glynch.ollama.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.glynch.ollama.Format;
import com.glynch.ollama.Options;
import com.glynch.ollama.chat.ChatRequest;
import com.glynch.ollama.chat.ChatResponse;
import com.glynch.ollama.chat.Message;
import com.glynch.ollama.create.CreateRequest;
import com.glynch.ollama.create.CreateResponse;
import com.glynch.ollama.embeddings.EmbeddingsRequest;
import com.glynch.ollama.embeddings.EmbeddingsResponse;
import com.glynch.ollama.generate.GenerateRequest;
import com.glynch.ollama.generate.GenerateResponse;
import com.glynch.ollama.list.Models;
import com.glynch.ollama.pull.PullRequest;
import com.glynch.ollama.pull.PullResponse;
import com.glynch.ollama.show.ShowRequest;
import com.glynch.ollama.show.ShowResponse;
import com.glynch.ollama.support.Body;

public class DefaultOllamaClient implements OllamaClient {

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String DEFAULT_OLLAMA_HOST = "http://localhost:11434";
    private static final String PING_PATH = "";
    private static final String LIST_PATH = "/api/tags";
    private static final String GENERATE_PATH = "/api/generate";
    private static final String CHAT_PATH = "/api/chat";
    private static final String SHOW_PATH = "/api/show";
    private static final String COPY_PATH = "/api/copy";
    private static final String CREATE_PATH = "/api/create";
    private static final String DELETE_PATH = "/api/copy";
    private static final String PULL_PATH = "/api/pull";
    private static final String PUSH_PATH = "/api/push";
    private static final String BLOBS_PATH = "/api/blobs";
    private static final String EMBEDDINGS_PATH = "/api/embeddings";
    private static final String LOAD = "load";

    private final String host;

    DefaultOllamaClient(String host) {
        this.host = host;
    }

    DefaultOllamaClient() {
        this(DEFAULT_OLLAMA_HOST);
    }

    @Override
    public String getHost() {
        return this.host;
    }

    private URI getUri(String path) {
        return URI.create(this.host + path);
    }

    private void onError(Throwable throwable) throws OllamaClientException {
        throw new OllamaClientException(throwable.getMessage());
    }

    private <T> HttpResponse<T> get(String path, BodyHandler<T> bodyHandler) throws OllamaClientException {
        HttpResponse<T> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri(path))
                .version(HttpClient.Version.HTTP_2)
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .GET()
                .build();
        try {
            response = client.send(request, bodyHandler);
        } catch (Exception e) {
            onError(e);
        }
        return response;
    }

    private <T> HttpResponse<Void> head(String path) throws OllamaClientException {
        HttpResponse<Void> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri(path))
                .version(HttpClient.Version.HTTP_2)
                .method("HEAD", BodyPublishers.noBody())
                .build();
        try {
            response = client.send(request, BodyHandlers.discarding());
        } catch (Exception e) {
            onError(e);
        }
        return response;
    }

    private <T> HttpResponse<T> post(String path, Object body,
            BodyHandler<T> bodyHandler) throws OllamaClientException {
        HttpResponse<T> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri(path))
                .version(HttpClient.Version.HTTP_2)
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .POST(Body.Publishers.json(body))
                .build();
        try {
            response = client.send(request, bodyHandler);
        } catch (Exception e) {
            onError(e);
        }
        return response;
    }

    private <T> HttpResponse<Stream<T>> stream(String path, Object body,
            BodyHandler<Stream<T>> bodyHandler) throws OllamaClientException {
        HttpResponse<Stream<T>> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri(path))
                .version(HttpClient.Version.HTTP_2)
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .POST(Body.Publishers.json(body))
                .build();
        try {
            response = client.send(request, bodyHandler);
        } catch (Exception e) {
            onError(e);
        }
        return response;
    }

    private <T> Stream<T> stream(String path, Object body, Class<T> type) throws OllamaClientException {
        return stream(path, body, Body.Handlers.streamOf(type)).body();
    }

    @Override
    public boolean ping() throws OllamaClientException {
        HttpResponse<String> response = get(PING_PATH, BodyHandlers.ofString());
        return response.statusCode() == 200;
    }

    @Override
    public Models list() throws OllamaClientException {
        return get(LIST_PATH, Body.Handlers.of(Models.class)).body();
    }

    @Override
    public boolean load(String model) {
        return LOAD.equals(generate(model, "").execute().findFirst().get().doneReason());
    }

    @Override
    public boolean blobs(String digest) {
        HttpResponse<Void> response = head(BLOBS_PATH + "/" + digest);
        return response.statusCode() == 200;
    }

    @Override
    public ShowResponse show(String name) {
        ShowRequest showRequest = new ShowRequest(name);
        return post(SHOW_PATH, showRequest, Body.Handlers.of(ShowResponse.class)).body();
    }

    @Override
    public GenerateSpec generate(String model, String prompt) {
        return new DefaultGenerateSpec(this, model, prompt);
    }

    @Override
    public ChatSpec chat(String model, Message message, Message... messages) {
        return new DefaultChatSpec(this, model, message, messages);
    }

    @Override
    public EmbeddingsSpec embeddings(String model, String prompt) {
        return new DefaultEmbeddingsSpec(this, model, prompt);
    }

    @Override
    public PullSpec pull(String name) {
        return new DefaultPullSpec(this, name);
    }

    private class DefaultGenerateSpec implements GenerateSpec {

        private final DefaultOllamaClient client;
        private String model;
        private String prompt;
        private List<String> images = new ArrayList<>();
        private Format format;
        private Options options;
        private String system;
        private String template;
        private List<Integer> context = new ArrayList<>();
        private Boolean stream;
        private Boolean raw;
        private String keepAlive;

        public DefaultGenerateSpec(DefaultOllamaClient ollamaClient, String model, String prompt) {
            this.client = ollamaClient;
            this.model = model;
            this.prompt = prompt;
        }

        @Override
        public GenerateSpec images(String image, String... images) {
            this.images.add(image);
            this.images.addAll(List.of(images));
            return this;
        }

        @Override
        public GenerateSpec image(String image) {
            this.images.add(image);
            return this;
        }

        @Override
        public GenerateSpec format(Format format) {
            this.format = format;
            return this;
        }

        @Override
        public GenerateSpec json() {
            return format(Format.JSON);
        }

        @Override
        public GenerateSpec options(Options options) {
            this.options = options;
            return this;
        }

        @Override
        public GenerateSpec system(String system) {
            this.system = system;
            return this;
        }

        @Override
        public GenerateSpec template(String template) {
            this.template = template;
            return this;
        }

        @Override
        public GenerateSpec context(int context, int... contexts) {
            this.context.add(context);
            for (int c : contexts) {
                this.context.add(c);
            }
            return this;
        }

        @Override
        public GenerateSpec stream(boolean stream) {
            this.stream = stream;
            return this;
        }

        @Override
        public GenerateSpec stream() {
            return stream(true);
        }

        @Override
        public GenerateSpec batch() {
            return stream(false);
        }

        @Override
        public GenerateSpec raw(boolean raw) {
            this.raw = raw;
            return this;
        }

        @Override
        public GenerateSpec raw() {
            return raw(true);
        }

        @Override
        public GenerateSpec keepAlive(String keepAlive) {
            this.keepAlive = keepAlive;
            return this;
        }

        @Override
        public Stream<GenerateResponse> execute() throws OllamaClientException {
            GenerateRequest generateRequest = new GenerateRequest(model, prompt, images, format, options, system,
                    template,
                    context,
                    stream, raw, keepAlive);

            System.out.println(generateRequest);
            return client.stream(GENERATE_PATH, generateRequest, GenerateResponse.class);
        }

    }

    private class DefaultEmbeddingsSpec implements EmbeddingsSpec {

        private final DefaultOllamaClient ollamaClient;
        private String model;
        private String prompt;
        private Options options;
        private String keepAlive;

        public DefaultEmbeddingsSpec(DefaultOllamaClient ollamaClient, String model, String prompt) {
            this.ollamaClient = ollamaClient;
            this.model = model;
            this.prompt = prompt;
        }

        @Override
        public EmbeddingsSpec options(Options options) {
            this.options = options;
            return this;
        }

        @Override
        public EmbeddingsSpec keepAlive(String keepAlive) {
            this.keepAlive = keepAlive;
            return this;
        }

        @Override
        public EmbeddingsResponse execute() throws OllamaClientException {
            EmbeddingsRequest request = new EmbeddingsRequest(model, prompt, options, keepAlive);
            return ollamaClient.post(EMBEDDINGS_PATH, request, Body.Handlers.of(EmbeddingsResponse.class)).body();
        }

    }

    private class DefaultChatSpec implements ChatSpec {

        private final DefaultOllamaClient ollamaClient;
        private final String model;
        private final List<Message> messages = new ArrayList<>();
        private Format format;
        private Options options;
        private Boolean stream;
        private String keepAlive;

        public DefaultChatSpec(DefaultOllamaClient ollamaClient, String model, Message message, Message... messages) {
            this.ollamaClient = ollamaClient;
            this.model = model;
            this.messages.add(message);
            this.messages.addAll(List.of(messages));
        }

        @Override
        public ChatSpec message(Message message) {
            this.messages.add(message);
            return this;
        }

        @Override
        public ChatSpec message(String message) {
            this.messages.add(Message.user(message));
            return this;
        }

        @Override
        public ChatSpec format(Format format) {
            this.format = format;
            return this;
        }

        @Override
        public ChatSpec options(Options options) {
            this.options = options;
            return this;
        }

        @Override
        public ChatSpec stream(boolean stream) {
            this.stream = stream;
            return this;
        }

        @Override
        public ChatSpec batch() {
            return stream(false);
        }

        @Override
        public ChatSpec keepAlive(String keepAlive) {
            this.keepAlive = keepAlive;
            return this;
        }

        @Override
        public Stream<ChatResponse> execute() throws OllamaClientException {
            ChatRequest chatRequest = new ChatRequest(model, messages, format, options, stream, keepAlive);
            return ollamaClient.stream(CHAT_PATH, chatRequest, ChatResponse.class);
        }

    }

    @Override
    public Stream<CreateResponse> create(String name, String modelfile) {
        CreateRequest createRequest = new CreateRequest(name, modelfile, true, null);
        return stream(CREATE_PATH, createRequest, CreateResponse.class);
    }

    private class DefaultPullSpec implements PullSpec {

        private final DefaultOllamaClient ollamaClient;
        private final String name;
        private Boolean insecure;
        private Boolean stream;

        public DefaultPullSpec(DefaultOllamaClient ollamaClient, String name) {
            this.ollamaClient = ollamaClient;
            this.name = name;
        }

        @Override
        public PullSpec insecure(boolean insecure) {
            this.insecure = insecure;
            return this;
        }

        @Override
        public PullSpec stream(boolean stream) {
            this.stream = stream;
            return this;
        }

        @Override
        public Stream<PullResponse> execute() throws OllamaClientException {
            PullRequest pullRequest = new PullRequest(name, insecure, stream);
            return ollamaClient.stream(PULL_PATH, pullRequest, PullResponse.class);
        }

    }

}
