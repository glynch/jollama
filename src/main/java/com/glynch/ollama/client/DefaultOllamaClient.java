package com.glynch.ollama.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import com.glynch.ollama.Format;
import com.glynch.ollama.Model;
import com.glynch.ollama.Options;
import com.glynch.ollama.chat.ChatHistoryResponse;
import com.glynch.ollama.chat.ChatRequest;
import com.glynch.ollama.chat.ChatResponse;
import com.glynch.ollama.chat.Message;
import com.glynch.ollama.copy.CopyRequest;
import com.glynch.ollama.create.CreateRequest;
import com.glynch.ollama.create.CreateResponse;
import com.glynch.ollama.delete.DeleteRequest;
import com.glynch.ollama.embeddings.EmbeddingsRequest;
import com.glynch.ollama.embeddings.EmbeddingsResponse;
import com.glynch.ollama.generate.GenerateRequest;
import com.glynch.ollama.generate.GenerateResponse;
import com.glynch.ollama.list.ListModel;
import com.glynch.ollama.list.ListModels;
import com.glynch.ollama.modelfile.InvalidModelFileException;
import com.glynch.ollama.modelfile.ModelFile;
import com.glynch.ollama.process.ProcessModel;
import com.glynch.ollama.process.ProcessModels;
import com.glynch.ollama.pull.PullRequest;
import com.glynch.ollama.pull.PullResponse;
import com.glynch.ollama.show.ShowRequest;
import com.glynch.ollama.show.ShowResponse;
import com.glynch.ollama.support.Body;

final class DefaultOllamaClient implements OllamaClient {

    String DEFAULT_OLLAMA_HOST = "http://localhost:11434";
    private static final String PING_PATH = "";
    private static final String LIST_PATH = "/api/tags";
    private static final String GENERATE_PATH = "/api/generate";
    private static final String CHAT_PATH = "/api/chat";
    private static final String SHOW_PATH = "/api/show";
    private static final String COPY_PATH = "/api/copy";
    private static final String CREATE_PATH = "/api/create";
    private static final String DELETE_PATH = "/api/delete";
    private static final String PULL_PATH = "/api/pull";
    private static final String PUSH_PATH = "/api/push";
    private static final String BLOBS_PATH = "/api/blobs";
    private static final String EMBEDDINGS_PATH = "/api/embeddings";
    private static final String PS_PATH = "/api/ps";

    private final String host;
    private final HttpClient client;

    DefaultOllamaClient(HttpClient client, String host) {
        this.host = host;
        this.client = client;
    }

    @Override
    public String getHost() {
        return this.host;
    }

    private URI getUri(String path) {
        return URI.create(this.host + path);
    }

    private void handleError(HttpRequest request, HttpResponse<?> response, Exception exception) {
        final Throwable cause = exception.getCause();
        final String message = cause.getMessage();

        if (cause instanceof OllamaClientResponseException) {
            throw (OllamaClientResponseException) cause;
        } else if (cause instanceof IOException) {
            throw new OllamaClientRequestException(message, cause, request.uri(), request.method());
        } else if (cause instanceof InterruptedException) {
            throw new OllamaClientException(message, cause);
        } else {
            throw new OllamaClientException(message, cause);
        }
    }

    private <T> HttpResponse<T> get(String path, BodyHandler<T> bodyHandler) throws OllamaClientException {
        HttpResponse<T> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri(path))
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .GET()
                .build();
        try {
            response = client.send(request, bodyHandler);
        } catch (Exception e) {
            handleError(request, response, e);
        }
        return response;
    }

    private <T> HttpResponse<Void> head(String path) throws OllamaClientException {
        HttpResponse<Void> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri(path))
                .method("HEAD", BodyPublishers.noBody())
                .build();
        try {
            response = client.send(request, BodyHandlers.discarding());
        } catch (Exception e) {
            handleError(request, response, e);
        }
        return response;
    }

    private <T> HttpResponse<Void> delete(String path, Object body) throws OllamaClientException {
        HttpResponse<Void> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri(path))
                .method("DELETE", Body.Publishers.json(body))
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .build();
        try {
            response = client.send(request, BodyHandlers.discarding());
        } catch (Exception e) {
            handleError(request, response, e);
        }
        return response;
    }

    private <T> HttpResponse<T> post(String path, Object body,
            BodyHandler<T> bodyHandler) throws OllamaClientException {
        HttpResponse<T> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri(path))
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .POST(Body.Publishers.json(body))
                .build();
        try {
            response = client.send(request, bodyHandler);
        } catch (Exception e) {
            handleError(request, response, e);
        }
        return response;
    }

    private <T> HttpResponse<Stream<T>> stream(String path, Object body,
            BodyHandler<Stream<T>> bodyHandler) throws OllamaClientException {
        System.out.println(body);
        HttpResponse<Stream<T>> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri(path))
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .POST(Body.Publishers.json(body))
                .build();
        try {
            response = client.send(request, bodyHandler);
        } catch (Exception e) {
            handleError(request, response, e);
        }
        return response;
    }

    private <T> Stream<T> stream(String path, Object body, Class<T> type) throws OllamaClientException {
        return stream(path, body, Body.Handlers.streamOf(type)).body();
    }

    @Override
    public boolean ping() throws OllamaClientException {
        boolean isUp = false;
        try {
            HttpResponse<String> response = get(PING_PATH, BodyHandlers.ofString());
            isUp = response.statusCode() == 200;
        } catch (Exception e) {

        }
        return isUp;
    }

    @Override
    public ProcessModels ps() throws OllamaClientException {
        return get(PS_PATH, Body.Handlers.of(ProcessModels.class)).body();
    }

    @Override
    public Optional<ProcessModel> ps(String name) throws OllamaClientException {
        Objects.requireNonNull(name, "name must not be null");
        return ps().models().stream().filter(model -> model.name().equals(name)).findFirst();
    }

    @Override
    public ListModels list() throws OllamaClientException {
        return get(LIST_PATH, Body.Handlers.of(ListModels.class)).body();
    }

    @Override
    public Optional<ListModel> list(String name) throws OllamaClientException {
        Objects.requireNonNull(name, "name must not be null");
        return list().models().stream().filter(model -> model.name().equals(name)).findFirst();
    }

    @Override
    public ProcessModel load(String model) throws OllamaClientException {
        Objects.requireNonNull(model, "model must not be null");
        generate(model, "").batch();
        return ps(model).get();
    }

    @Override
    public ProcessModel load(Model model) throws OllamaClientException {
        Objects.requireNonNull(model, "model must not be null");
        generate(model.toString(), "").batch();
        return ps(model.toString()).get();
    }

    @Override
    public BlobsSpec blobs(String digest) {
        Objects.requireNonNull(digest, "digest must not be null");
        return new DefaultBlobsSpec(this, digest);
    }

    @Override
    public ModelFile show(String name) throws OllamaClientException, InvalidModelFileException {
        Objects.requireNonNull(name, "name must not be null");
        ShowRequest showRequest = new ShowRequest(name);
        ShowResponse showResponse = post(SHOW_PATH, showRequest, Body.Handlers.of(ShowResponse.class)).body();
        return ModelFile.parse(showResponse.modelfile());
    }

    @Override
    public GenerateSpec generate(String model, String prompt) {
        Objects.requireNonNull(model, "model must not be null");
        Objects.requireNonNull(prompt, "prompt must not be null");
        return new DefaultGenerateSpec(this, model, prompt);
    }

    @Override
    public ChatSpec chat(String model, String prompt) {
        Objects.requireNonNull(model, "model must not be null");
        Objects.requireNonNull(prompt, "message must not be null");
        Message message = Message.user(prompt);
        return new DefaultChatSpec(this, model, message);
    }

    @Override
    public EmbeddingsSpec embeddings(String model, String prompt) {
        Objects.requireNonNull(model, "model must not be null");
        Objects.requireNonNull(prompt, "prompt must not be null");
        return new DefaultEmbeddingsSpec(this, model, prompt);
    }

    @Override
    public PullSpec pull(String name) {
        Objects.requireNonNull(name, "name must not be null");
        return new DefaultPullSpec(this, name);
    }

    @Override
    public int copy(String source, String destination) throws OllamaClientException {
        Objects.requireNonNull(source, "source must not be null");
        Objects.requireNonNull(destination, "destination must not be null");
        CopyRequest copyRequest = new CopyRequest(source, destination);
        return post(COPY_PATH, copyRequest, BodyHandlers.discarding()).statusCode();
    }

    @Override
    public int delete(String name) throws OllamaClientException {
        Objects.requireNonNull(name, "name must not be null");
        DeleteRequest deleteRequest = new DeleteRequest(name);
        return delete(DELETE_PATH, deleteRequest).statusCode();
    }

    private class DefaultGenerateSpec implements GenerateSpec {

        private final DefaultOllamaClient ollamaClient;
        private String model;
        private String prompt;
        private List<String> images = new ArrayList<>();
        private Format format;
        private Options options;
        private String system;
        private String template;
        private List<Integer> context = new ArrayList<>();
        private Boolean raw;
        private String keepAlive;

        public DefaultGenerateSpec(DefaultOllamaClient ollamaClient, String model, String prompt) {
            this.ollamaClient = ollamaClient;
            this.model = model;
            this.prompt = prompt;
        }

        @Override
        public GenerateSpec images(String image, String... images) {
            Objects.requireNonNull(image, "image must not be null");
            this.images.add(image);
            this.images.addAll(List.of(images));
            return this;
        }

        @Override
        public GenerateSpec image(String image) {
            Objects.requireNonNull(image, "image must not be null");
            this.images.add(image);
            return this;
        }

        @Override
        public GenerateSpec format(Format format) {
            Objects.requireNonNull(format, "format must not be null");
            this.format = format;
            return this;
        }

        @Override
        public GenerateSpec json() {
            return format(Format.JSON);
        }

        @Override
        public GenerateSpec options(Options options) {
            Objects.requireNonNull(options, "options must not be null");
            this.options = options;
            return this;
        }

        @Override
        public GenerateSpec system(String system) {
            Objects.requireNonNull(system, "system must not be null");
            this.system = system;
            return this;
        }

        @Override
        public GenerateSpec template(String template) {
            Objects.requireNonNull(template, "template must not be null");
            this.template = template;
            return this;
        }

        @Override
        public GenerateSpec context(int context, int... contexts) {
            Objects.requireNonNull(context, "context must not be null");
            this.context.add(context);
            for (int c : contexts) {
                this.context.add(c);
            }
            return this;
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
            Objects.requireNonNull(keepAlive, "keepAlive must not be null");
            this.keepAlive = keepAlive;
            return this;
        }

        @Override
        public Stream<GenerateResponse> stream() {
            GenerateRequest generateRequest = new GenerateRequest(model, prompt, images, format, options, system,
                    template,
                    context,
                    true, raw, keepAlive);
            return ollamaClient.stream(GENERATE_PATH, generateRequest, GenerateResponse.class);
        }

        @Override
        public GenerateResponse batch() {
            GenerateRequest generateRequest = new GenerateRequest(model, prompt, images, format, options, system,
                    template,
                    context,
                    false, raw, keepAlive);
            return ollamaClient.post(GENERATE_PATH, generateRequest, Body.Handlers.of(GenerateResponse.class)).body();
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
            Objects.requireNonNull(options, "options must not be null");
            this.options = options;
            return this;
        }

        @Override
        public EmbeddingsSpec keepAlive(String keepAlive) {
            Objects.requireNonNull(keepAlive, "keepAlive must not be null");
            this.keepAlive = keepAlive;
            return this;
        }

        @Override
        public EmbeddingsResponse get() throws OllamaClientException {
            EmbeddingsRequest request = new EmbeddingsRequest(model, prompt, options, keepAlive);
            return ollamaClient.post(EMBEDDINGS_PATH, request, Body.Handlers.of(EmbeddingsResponse.class)).body();
        }

    }

    private class DefaultChatSpec implements ChatSpec {

        private final DefaultOllamaClient ollamaClient;
        private final String model;
        private final List<Message> messages = new ArrayList<>();
        private final Message message;
        private Format format;
        private Options options;
        private String keepAlive;

        public DefaultChatSpec(DefaultOllamaClient ollamaClient, String model, Message message) {
            this.ollamaClient = ollamaClient;
            this.model = model;
            this.message = message;
        }

        @Override
        public ChatSpec history(Message... messages) {
            Objects.requireNonNull(messages, "messages must not be null");
            this.messages.addAll(List.of(messages));
            return this;
        }

        @Override
        public ChatSpec history(List<Message> messages) {
            Objects.requireNonNull(messages, "messages must not be null");
            this.messages.addAll(messages);
            return this;
        }

        @Override
        public ChatSpec format(Format format) {
            Objects.requireNonNull(format, "format must not be null");
            this.format = format;
            return this;
        }

        @Override
        public ChatSpec options(Options options) {
            Objects.requireNonNull(options, "options must not be null");
            this.options = options;
            return this;
        }

        @Override
        public ChatSpec keepAlive(String keepAlive) {
            Objects.requireNonNull(keepAlive, "keepAlive must not be null");
            this.keepAlive = keepAlive;
            return this;
        }

        @Override
        public Stream<ChatResponse> stream() throws OllamaClientException {
            messages.add(message);
            ChatRequest chatRequest = new ChatRequest(model, messages, format, options, true, keepAlive);
            return ollamaClient.stream(CHAT_PATH, chatRequest, ChatResponse.class);
        }

        @Override
        public ChatHistoryResponse batch() throws OllamaClientException {
            messages.add(message);
            ChatRequest chatRequest = new ChatRequest(model, messages, format, options, false, keepAlive);
            ChatResponse chatResponse = ollamaClient.post(CHAT_PATH, chatRequest, Body.Handlers.of(ChatResponse.class))
                    .body();
            return new ChatHistoryResponse(chatResponse, messages);
        }

    }

    @Override
    public CreateSpec create(String name, String modelfile) throws InvalidModelFileException {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(modelfile, "modelfile must not be null");

        return new DefaultCreateSpec(this, name, ModelFile.parse(modelfile));
    }

    @Override
    public CreateSpec create(String name, Path path) throws InvalidModelFileException, IOException {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(path, "path must not be null");
        return new DefaultCreateSpec(this, name, ModelFile.parse(path));
    }

    @Override
    public CreateSpec create(String name, ModelFile modelFile) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(modelFile, "modelFile must not be null");
        return new DefaultCreateSpec(this, name, modelFile);
    }

    private class DefaultCreateSpec implements CreateSpec {

        private final DefaultOllamaClient ollamaClient;
        private final String name;
        private String modelfile;
        private Path path;

        public DefaultCreateSpec(DefaultOllamaClient ollamaClient, String name, String modelfile) {
            this.ollamaClient = ollamaClient;
            this.name = name;
            this.modelfile = modelfile;
        }

        public DefaultCreateSpec(DefaultOllamaClient ollamaClient, String name, Path path) {
            this.ollamaClient = ollamaClient;
            this.name = name;
            this.path = path;
        }

        public DefaultCreateSpec(DefaultOllamaClient ollamaClient, String name, ModelFile modelFile) {
            this.ollamaClient = ollamaClient;
            this.name = name;
            this.modelfile = modelFile.toString();
        }

        @Override
        public Stream<CreateResponse> stream() {
            CreateRequest createRequest = new CreateRequest(name, modelfile, true, path);
            return ollamaClient.stream(CREATE_PATH, createRequest, CreateResponse.class);
        }

        @Override
        public CreateResponse batch() {
            CreateRequest createRequest = new CreateRequest(name, modelfile, false, path);

            return ollamaClient.post(CREATE_PATH, createRequest, Body.Handlers.of(CreateResponse.class)).body();
        }

    }

    private class DefaultPullSpec implements PullSpec {

        private final DefaultOllamaClient ollamaClient;
        private final String name;
        private Boolean insecure;

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
        public Stream<PullResponse> stream() throws OllamaClientException {
            PullRequest pullRequest = new PullRequest(name, insecure, true);
            return ollamaClient.stream(PULL_PATH, pullRequest, PullResponse.class);
        }

        @Override
        public PullResponse batch() throws OllamaClientException {
            return ollamaClient.post(PULL_PATH, new PullRequest(name, insecure, false),
                    Body.Handlers.of(PullResponse.class)).body();
        }

    }

    private class DefaultBlobsSpec implements BlobsSpec {

        private final DefaultOllamaClient ollamaClient;
        private final String digest;

        public DefaultBlobsSpec(DefaultOllamaClient ollamaClient, String digest) {
            this.ollamaClient = ollamaClient;
            this.digest = digest;
        }

        @Override
        public int exists() throws OllamaClientException {
            HttpResponse<Void> response = head(BLOBS_PATH + "/" + digest);
            return response.statusCode();
        }

        @Override
        public int create() throws OllamaClientException {
            HttpResponse<Void> response = ollamaClient.post(BLOBS_PATH + "/" + digest, digest,
                    BodyHandlers.discarding());
            return response.statusCode();
        }

    }

}
