package com.glynch.jollama.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glynch.jollama.Format;
import com.glynch.jollama.KeepAlive;
import com.glynch.jollama.Model;
import com.glynch.jollama.Options;
import com.glynch.jollama.chat.ChatRequest;
import com.glynch.jollama.chat.ChatResponse;
import com.glynch.jollama.chat.Message;
import com.glynch.jollama.chat.history.InMemoryMessageHistory;
import com.glynch.jollama.chat.history.MessageHistory;
import com.glynch.jollama.copy.CopyRequest;
import com.glynch.jollama.create.CreateRequest;
import com.glynch.jollama.create.CreateResponse;
import com.glynch.jollama.delete.DeleteRequest;
import com.glynch.jollama.embeddings.EmbeddingsRequest;
import com.glynch.jollama.embeddings.EmbeddingsResponse;
import com.glynch.jollama.generate.GenerateRequest;
import com.glynch.jollama.generate.GenerateResponse;
import com.glynch.jollama.list.ListModel;
import com.glynch.jollama.list.ListModels;
import com.glynch.jollama.modelfile.InvalidModelFileException;
import com.glynch.jollama.modelfile.ModelFile;
import com.glynch.jollama.process.ProcessModel;
import com.glynch.jollama.process.ProcessModels;
import com.glynch.jollama.pull.PullRequest;
import com.glynch.jollama.pull.PullResponse;
import com.glynch.jollama.show.ShowRequest;
import com.glynch.jollama.show.ShowResponse;
import com.glynch.jollama.support.Body;

import reactor.core.publisher.Flux;

final class DefaultJOllamaClient implements JOllamaClient {

    private static final Logger LOGGER = LoggerFactory.getLogger("com.glynch.ollama.client");
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

    DefaultJOllamaClient(HttpClient client, String host) {
        this.host = host;
        this.client = client;
    }

    @Override
    public String getHost() {
        return this.host;
    }

    @Override
    public Optional<Duration> getConnectTimeout() {
        return this.client.connectTimeout();
    }

    private URI getUri(String path) {
        return URI.create(this.host + path);
    }

    private void handleError(HttpRequest request, HttpResponse<?> response, Exception exception) {
        final Throwable cause = exception.getCause();
        final String message = cause.getMessage();

        if (cause instanceof JOllamaClientResponseException) {
            throw (JOllamaClientResponseException) cause;
        } else if (cause instanceof IOException) {
            throw new JOllamaClientRequestException(message, cause, request.uri(), request.method());
        } else if (cause instanceof InterruptedException) {
            throw new JOllamaClientException(message, cause);
        } else {
            throw new JOllamaClientException(message, cause);
        }
    }

    private <T> HttpResponse<T> get(String path, BodyHandler<T> bodyHandler) throws JOllamaClientException {
        LOGGER.debug("GET {}", path);
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

    private <T> HttpResponse<Void> head(String path) throws JOllamaClientException {
        LOGGER.debug("HEAD {}", path);
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

    private <T> HttpResponse<Void> delete(String path, Object body) throws JOllamaClientException {
        LOGGER.debug("DELETE {}: {}", path, body);
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
            BodyHandler<T> bodyHandler) throws JOllamaClientException {
        LOGGER.debug("POST {}: {}", path, body);
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
            BodyHandler<Stream<T>> bodyHandler) throws JOllamaClientException {
        LOGGER.debug("POST {}: {}", path, body);
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

    private <T> Stream<T> stream(String path, Object body, Class<T> type) throws JOllamaClientException {
        return stream(path, body, Body.Handlers.streamOf(type)).body();
    }

    @Override
    public boolean ping() throws JOllamaClientException {
        boolean isUp = false;
        try {
            HttpResponse<String> response = get(PING_PATH, BodyHandlers.ofString());
            isUp = response.statusCode() == 200;
        } catch (Exception e) {

        }
        return isUp;
    }

    @Override
    public ProcessModels ps() throws JOllamaClientException {
        return get(PS_PATH, Body.Handlers.of(ProcessModels.class)).body();
    }

    @Override
    public Optional<ProcessModel> ps(String name) throws JOllamaClientException {
        Objects.requireNonNull(name, "name must not be null");
        return ps().models().stream().filter(model -> model.name().equals(name)).findFirst();
    }

    @Override
    public ListModels list() throws JOllamaClientException {
        return get(LIST_PATH, Body.Handlers.of(ListModels.class)).body();
    }

    @Override
    public Optional<ListModel> list(Model model) throws JOllamaClientException {
        Objects.requireNonNull(model, "model must not be null");
        return list(model.toString());
    }

    @Override
    public Optional<ListModel> list(String name) throws JOllamaClientException {
        Objects.requireNonNull(name, "name must not be null");
        return list().models().stream().filter(model -> model.name().equals(name)).findFirst();
    }

    @Override
    public Optional<ProcessModel> load(String model, KeepAlive keepAlive) throws JOllamaClientException {
        Objects.requireNonNull(model, "model must not be null");
        Objects.requireNonNull(keepAlive, "keepAlive must not be null");
        generate(model, "").keepAlive(keepAlive).batch();
        return ps(model);
    }

    @Override
    public Optional<ProcessModel> load(String model) throws JOllamaClientException {
        Objects.requireNonNull(model, "model must not be null");
        return load(model, KeepAlive.DEFAULT);
    }

    @Override
    public Optional<ProcessModel> load(Model model) throws JOllamaClientException {
        Objects.requireNonNull(model, "model must not be null");
        return load(model.toString());
    }

    @Override
    public Optional<ProcessModel> load(Model model, KeepAlive keepAlive) throws JOllamaClientException {
        Objects.requireNonNull(model, "model must not be null");
        return load(model.toString(), keepAlive);
    }

    @Override
    public BlobsSpec blobs(String digest) {
        Objects.requireNonNull(digest, "digest must not be null");
        return new DefaultBlobsSpec(this, digest);
    }

    @Override
    public ModelFile show(String name) throws JOllamaClientException, InvalidModelFileException {
        Objects.requireNonNull(name, "name must not be null");
        ShowRequest showRequest = new ShowRequest(name);
        ShowResponse showResponse = post(SHOW_PATH, showRequest, Body.Handlers.of(ShowResponse.class)).body();
        return ModelFile.parse(showResponse.modelfile());
    }

    @Override
    public ModelFile show(Model name) throws JOllamaClientException, InvalidModelFileException {
        Objects.requireNonNull(name, "name must not be null");
        return show(name.toString());
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
    public ChatSpec chat(Model model, String prompt) {
        Objects.requireNonNull(model, "model must not be null");
        return chat(model.toString(), prompt);
    }

    @Override
    public ChatSpec chat(String model, String prompt, String... images) {
        Objects.requireNonNull(model, "model must not be null");
        Objects.requireNonNull(prompt, "message must not be null");
        Message message = Message.user(prompt, images);
        return new DefaultChatSpec(this, model, message);
    }

    @Override
    public ChatSpec chat(Model model, String prompt, String... images) {
        Objects.requireNonNull(model, "model must not be null");
        return chat(model.toString(), prompt, images);
    }

    @Override
    public ChatSpec chat(String model, String prompt, List<String> images) {
        Objects.requireNonNull(model, "model must not be null");
        Objects.requireNonNull(prompt, "message must not be null");
        Message message = Message.user(prompt, images);
        return new DefaultChatSpec(this, model, message);
    }

    @Override
    public ChatSpec chat(Model model, String prompt, List<String> images) {
        Objects.requireNonNull(model, "model must not be null");
        return chat(model.toString(), prompt, images);
    }

    @Override
    public EmbeddingsSpec embeddings(String model, String prompt) {
        Objects.requireNonNull(model, "model must not be null");
        Objects.requireNonNull(prompt, "prompt must not be null");
        return new DefaultEmbeddingsSpec(this, model, prompt);
    }

    @Override
    public EmbeddingsSpec embeddings(Model model, String prompt) {
        Objects.requireNonNull(model, "model must not be null");
        return embeddings(model.toString(), prompt);
    }

    @Override
    public PullSpec pull(String name) {
        Objects.requireNonNull(name, "name must not be null");
        return new DefaultPullSpec(this, name);
    }

    @Override
    public PullSpec pull(Model name) {
        Objects.requireNonNull(name, "name must not be null");
        return pull(name.toString());
    }

    @Override
    public int copy(String source, String destination) throws JOllamaClientException {
        Objects.requireNonNull(source, "source must not be null");
        Objects.requireNonNull(destination, "destination must not be null");
        CopyRequest copyRequest = new CopyRequest(source, destination);
        return post(COPY_PATH, copyRequest, BodyHandlers.discarding()).statusCode();
    }

    @Override
    public int copy(Model source, String destination) throws JOllamaClientException {
        Objects.requireNonNull(source, "source must not be null");
        return copy(source.toString(), destination);
    }

    @Override
    public int delete(String name) throws JOllamaClientException {
        Objects.requireNonNull(name, "name must not be null");
        DeleteRequest deleteRequest = new DeleteRequest(name);
        return delete(DELETE_PATH, deleteRequest).statusCode();
    }

    @Override
    public int delete(Model name) throws JOllamaClientException {
        Objects.requireNonNull(name, "name must not be null");
        return delete(name.toString());
    }

    private class DefaultGenerateSpec implements GenerateSpec {

        private final DefaultJOllamaClient ollamaClient;
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

        public DefaultGenerateSpec(DefaultJOllamaClient ollamaClient, String model, String prompt) {
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
        public GenerateSpec keepAlive(KeepAlive keepAlive) {
            Objects.requireNonNull(keepAlive, "keepAlive must not be null");
            this.keepAlive = keepAlive.toString();
            return this;
        }

        @Override
        public Flux<GenerateResponse> stream() {
            GenerateRequest generateRequest = new GenerateRequest(model, prompt, images, format, options, system,
                    template,
                    context,
                    true, raw, keepAlive);
            Stream<GenerateResponse> response = ollamaClient.stream(GENERATE_PATH, generateRequest,
                    GenerateResponse.class);
            return Flux.fromStream(response);
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

        private final DefaultJOllamaClient ollamaClient;
        private String model;
        private String prompt;
        private Options options;
        private String keepAlive;

        public DefaultEmbeddingsSpec(DefaultJOllamaClient ollamaClient, String model, String prompt) {
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
        public EmbeddingsResponse get() throws JOllamaClientException {
            EmbeddingsRequest request = new EmbeddingsRequest(model, prompt, options, keepAlive);
            return ollamaClient.post(EMBEDDINGS_PATH, request, Body.Handlers.of(EmbeddingsResponse.class)).body();
        }

    }

    private class DefaultChatSpec implements ChatSpec {

        private final DefaultJOllamaClient ollamaClient;
        private final String model;
        private final Message message;
        private String system;
        private MessageHistory history = new InMemoryMessageHistory();
        private Format format;
        private Options options;
        private String keepAlive;

        public DefaultChatSpec(DefaultJOllamaClient ollamaClient, String model, Message message) {
            this.ollamaClient = ollamaClient;
            this.model = model;
            this.message = message;
        }

        @Override
        public ChatSpec system(String system) {
            Objects.requireNonNull(system, "system must not be null");
            this.system = system;
            return this;
        }

        @Override
        public ChatSpec history(Message... messages) {
            Objects.requireNonNull(messages, "messages must not be null");
            history.add(List.of(messages));
            return this;
        }

        @Override
        public ChatSpec history(List<Message> messages) {
            Objects.requireNonNull(messages, "messages must not be null");
            history.add(messages);
            return this;
        }

        @Override
        public ChatSpec history(MessageHistory history) {
            Objects.requireNonNull(history, "history must not be null");
            this.history = history;
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
        public ChatSpec keepAlive(KeepAlive keepAlive) {
            Objects.requireNonNull(keepAlive, "keepAlive must not be null");
            this.keepAlive = keepAlive.toString();
            return this;
        }

        @Override
        public Flux<ChatResponse> stream() throws JOllamaClientException {
            if (system != null) {
                history.add(Message.system(system));
            }
            history.add(message);

            ChatRequest chatRequest = new ChatRequest(model, history.messages(), format, options, true, keepAlive);
            StringBuilder content = new StringBuilder();
            Stream<ChatResponse> stream = ollamaClient.stream(CHAT_PATH, chatRequest, ChatResponse.class)
                    .map(r -> {
                        content.append(r.message().content());
                        return r;
                    });
            return Flux.fromStream(stream).doOnComplete(() -> {
                history.add(Message.assistant(content.toString()));
            });
        }

        @Override
        public ChatResponse batch() throws JOllamaClientException {
            if (system != null) {
                history.add(Message.system(system));
            }
            history.add(message);
            ChatRequest chatRequest = new ChatRequest(model, history.messages(), format, options, false, keepAlive);
            ChatResponse response = ollamaClient.post(CHAT_PATH, chatRequest, Body.Handlers.of(ChatResponse.class))
                    .body();
            history.add(response.message());
            return response;
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

        private final DefaultJOllamaClient ollamaClient;
        private final String name;
        private String modelfile;
        private Path path;

        public DefaultCreateSpec(DefaultJOllamaClient ollamaClient, String name, ModelFile modelFile) {
            this.ollamaClient = ollamaClient;
            this.name = name;
            this.modelfile = modelFile.toString();
        }

        @Override
        public Flux<CreateResponse> stream() {
            CreateRequest createRequest = new CreateRequest(name, modelfile, true, path);
            return Flux.fromStream(ollamaClient.stream(CREATE_PATH, createRequest, CreateResponse.class));
        }

        @Override
        public CreateResponse batch() {
            CreateRequest createRequest = new CreateRequest(name, modelfile, false, path);

            return ollamaClient.post(CREATE_PATH, createRequest, Body.Handlers.of(CreateResponse.class)).body();
        }

    }

    private class DefaultPullSpec implements PullSpec {

        private final DefaultJOllamaClient ollamaClient;
        private final String name;
        private Boolean insecure;

        public DefaultPullSpec(DefaultJOllamaClient ollamaClient, String name) {
            this.ollamaClient = ollamaClient;
            this.name = name;
        }

        @Override
        public PullSpec insecure(boolean insecure) {
            this.insecure = insecure;
            return this;
        }

        @Override
        public Flux<PullResponse> stream() throws JOllamaClientException {
            PullRequest pullRequest = new PullRequest(name, insecure, true);
            return Flux.fromStream(ollamaClient.stream(PULL_PATH, pullRequest, PullResponse.class));
        }

        @Override
        public PullResponse batch() throws JOllamaClientException {
            return ollamaClient.post(PULL_PATH, new PullRequest(name, insecure, false),
                    Body.Handlers.of(PullResponse.class)).body();
        }

    }

    private class DefaultBlobsSpec implements BlobsSpec {

        private final DefaultJOllamaClient ollamaClient;
        private final String digest;

        public DefaultBlobsSpec(DefaultJOllamaClient ollamaClient, String digest) {
            this.ollamaClient = ollamaClient;
            this.digest = digest;
        }

        @Override
        public int exists() throws JOllamaClientException {
            HttpResponse<Void> response = head(BLOBS_PATH + "/" + digest);
            return response.statusCode();
        }

        @Override
        public int create() throws JOllamaClientException {
            HttpResponse<Void> response = ollamaClient.post(BLOBS_PATH + "/" + digest, digest,
                    BodyHandlers.discarding());
            return response.statusCode();
        }

    }

}
