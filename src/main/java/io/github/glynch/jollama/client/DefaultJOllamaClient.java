package io.github.glynch.jollama.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import io.github.glynch.jollama.Format;
import io.github.glynch.jollama.KeepAlive;
import io.github.glynch.jollama.Model;
import io.github.glynch.jollama.Options;
import io.github.glynch.jollama.chat.ChatRequest;
import io.github.glynch.jollama.chat.ChatResponse;
import io.github.glynch.jollama.chat.Message;
import io.github.glynch.jollama.chat.history.InMemoryMessageHistory;
import io.github.glynch.jollama.chat.history.MessageHistory;
import io.github.glynch.jollama.client.api.DefaultJOllamaApi;
import io.github.glynch.jollama.client.api.JOllamaApi;
import io.github.glynch.jollama.copy.CopyRequest;
import io.github.glynch.jollama.create.CreateRequest;
import io.github.glynch.jollama.create.CreateResponse;
import io.github.glynch.jollama.delete.DeleteRequest;
import io.github.glynch.jollama.embeddings.EmbeddingsRequest;
import io.github.glynch.jollama.embeddings.EmbeddingsResponse;
import io.github.glynch.jollama.generate.GenerateRequest;
import io.github.glynch.jollama.generate.GenerateResponse;
import io.github.glynch.jollama.list.ListModel;
import io.github.glynch.jollama.list.ListModels;
import io.github.glynch.jollama.modelfile.InvalidModelFileException;
import io.github.glynch.jollama.modelfile.ModelFile;
import io.github.glynch.jollama.process.ProcessModel;
import io.github.glynch.jollama.process.ProcessModels;
import io.github.glynch.jollama.pull.PullRequest;
import io.github.glynch.jollama.pull.PullResponse;
import io.github.glynch.jollama.show.ShowRequest;
import io.github.glynch.jollama.show.ShowResponse;
import io.github.glynch.jollama.support.DigestUtils;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import reactor.core.publisher.Flux;

final class DefaultJOllamaClient implements JOllamaClient {

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
    private final JOllamaApi api;

    DefaultJOllamaClient(OkHttpClient client, String host) {
        this.host = host;
        this.api = new DefaultJOllamaApi(client, host);
    }

    @Override
    public String getHost() {
        return this.host;
    }

    @Override
    public Redirect getRedirect() {
        if (this.api.followSslRedirects() && this.api.followRedirects()) {
            return Redirect.ALWAYS;
        } else if (this.api.followRedirects()) {
            return Redirect.NORMAL;
        } else {
            return Redirect.NEVER;
        }

    }

    @Override
    public int getConnectTimeout() {
        return api.connectTimeout();
    }

    @Override
    public int getReadTimeout() {
        return api.readTimeout();
    }

    @Override
    public boolean ping() {
        try {
            Response response = api.get(PING_PATH);
            return response.isSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public ProcessModels ps() throws JOllamaClientException {
        return api.get(PS_PATH, ProcessModels.class);
    }

    @Override
    public Optional<ProcessModel> ps(String name) throws JOllamaClientException {
        Objects.requireNonNull(name, "name must not be null");
        return ps().models().stream().filter(model -> model.name().equals(name)).findFirst();
    }

    @Override
    public ListModels list() throws JOllamaClientException {
        return api.get(LIST_PATH, ListModels.class);
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
    public BlobsSpec blobs() {
        return new DefaultBlobsSpec(api);
    }

    @Override
    public ShowResponse show(String name, boolean verbose) throws JOllamaClientException, InvalidModelFileException {
        Objects.requireNonNull(name, "name must not be null");
        ShowRequest showRequest = new ShowRequest(name, verbose);
        return api.post(SHOW_PATH, showRequest, ShowResponse.class);
    }

    @Override
    public ShowResponse show(String name) throws JOllamaClientException, InvalidModelFileException {
        Objects.requireNonNull(name, "name must not be null");
        return show(name, false);
    }

    @Override
    public ShowResponse show(Model model) throws JOllamaClientException, InvalidModelFileException {
        Objects.requireNonNull(model, "model must not be null");
        return show(model.toString());
    }

    @Override
    public ShowResponse show(Model model, boolean verbose) throws JOllamaClientException, InvalidModelFileException {
        Objects.requireNonNull(model, "model must not be null");
        Objects.requireNonNull(verbose, "verbose must not be null");
        return show(model.toString(), verbose);
    }

    @Override
    public GenerateSpec generate(String model, String prompt) {
        Objects.requireNonNull(model, "model must not be null");
        Objects.requireNonNull(prompt, "prompt must not be null");
        return new DefaultGenerateSpec(api, model, prompt);
    }

    @Override
    public GenerateSpec generate(Model model, String prompt) {
        Objects.requireNonNull(model, "model must not be null");
        return generate(model.toString(), prompt);
    }

    @Override
    public ChatSpec chat(String model, String prompt) {
        Objects.requireNonNull(model, "model must not be null");
        Objects.requireNonNull(prompt, "prompt must not be null");
        Message message = Message.user(prompt);
        return new DefaultChatSpec(api, model, message);
    }

    @Override
    public ChatSpec chat(Model model, String prompt) {
        Objects.requireNonNull(model, "model must not be null");
        return chat(model.toString(), prompt);
    }

    @Override
    public ChatSpec chat(String model, String prompt, String... images) {
        Objects.requireNonNull(model, "model must not be null");
        Objects.requireNonNull(prompt, "prompt must not be null");
        Message message = Message.user(prompt, images);
        return new DefaultChatSpec(api, model, message);
    }

    @Override
    public ChatSpec chat(Model model, String prompt, String... images) {
        Objects.requireNonNull(model, "model must not be null");
        return chat(model.toString(), prompt, images);
    }

    @Override
    public ChatSpec chat(String model, String prompt, List<String> images) {
        Objects.requireNonNull(model, "model must not be null");
        Objects.requireNonNull(prompt, "prompt must not be null");
        Message message = Message.user(prompt, images);
        return new DefaultChatSpec(api, model, message);
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
        return new DefaultEmbeddingsSpec(api, model, prompt);
    }

    @Override
    public EmbeddingsSpec embeddings(Model model, String prompt) {
        Objects.requireNonNull(model, "model must not be null");
        return embeddings(model.toString(), prompt);
    }

    @Override
    public PullSpec pull(String name) {
        Objects.requireNonNull(name, "name must not be null");
        return new DefaultPullSpec(api, name);
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
        return api.post(COPY_PATH, copyRequest).code();
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
        return api.delete(DELETE_PATH, deleteRequest).code();
    }

    @Override
    public int delete(Model name) throws JOllamaClientException {
        Objects.requireNonNull(name, "name must not be null");
        return delete(name.toString());
    }

    private class DefaultGenerateSpec implements GenerateSpec {

        private final JOllamaApi api;
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

        public DefaultGenerateSpec(JOllamaApi api, String model, String prompt) {
            this.api = api;
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
            Flux<GenerateResponse> response = api.stream(GENERATE_PATH, generateRequest,
                    GenerateResponse.class);
            return response;
        }

        @Override
        public GenerateResponse batch() {
            GenerateRequest generateRequest = new GenerateRequest(model, prompt, images, format, options, system,
                    template,
                    context,
                    false, raw, keepAlive);
            return api.post(GENERATE_PATH, generateRequest, GenerateResponse.class);
        }

    }

    private class DefaultEmbeddingsSpec implements EmbeddingsSpec {

        private final JOllamaApi api;
        private String model;
        private String prompt;
        private Options options;
        private String keepAlive;

        public DefaultEmbeddingsSpec(JOllamaApi api, String model, String prompt) {
            this.api = api;
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
            return api.post(EMBEDDINGS_PATH, request, EmbeddingsResponse.class);
        }

    }

    private class DefaultChatSpec implements ChatSpec {

        private final JOllamaApi api;
        private final String model;
        private final Message message;
        private String system;
        private MessageHistory history = new InMemoryMessageHistory();
        private Format format;
        private Options options;
        private String keepAlive;

        public DefaultChatSpec(JOllamaApi api, String model, Message message) {
            this.api = api;
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
            Flux<ChatResponse> flux = api.stream(CHAT_PATH, chatRequest, ChatResponse.class)
                    .map(r -> {
                        content.append(r.message().content());
                        return r;
                    });
            return flux.doOnComplete(() -> history.add(Message.assistant(content.toString())));
        }

        @Override
        public ChatResponse batch() throws JOllamaClientException {
            if (system != null) {
                history.add(Message.system(system));
            }
            history.add(message);
            ChatRequest chatRequest = new ChatRequest(model, history.messages(), format, options, false, keepAlive);
            ChatResponse response = api.post(CHAT_PATH, chatRequest, ChatResponse.class);
            history.add(response.message());
            return response;
        }

    }

    @Override
    public CreateSpec create(String name, ModelFile modelfile) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(modelfile, "modelfile must not be null");

        return new DefaultCreateSpec(api, name, modelfile);
    }

    @Override
    public CreateSpec create(String name, Path path) throws InvalidModelFileException, IOException {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(path, "path must not be null");
        return new DefaultCreateSpec(api, name, ModelFile.parse(path));
    }

    private class DefaultCreateSpec implements CreateSpec {

        private final JOllamaApi api;
        private final String name;
        private final ModelFile modelfile;

        public DefaultCreateSpec(JOllamaApi api, String name, ModelFile modelFile) {
            this.api = api;
            this.name = name;
            this.modelfile = modelFile;
        }

        @Override
        public Flux<CreateResponse> stream() {
            createBlobs(modelfile);
            CreateRequest createRequest = new CreateRequest(name, modelfile.toString(), true);
            return api.stream(CREATE_PATH, createRequest, CreateResponse.class);
        }

        @Override
        public CreateResponse batch() {
            createBlobs(modelfile);
            CreateRequest createRequest = new CreateRequest(name, modelfile.toString(), false);
            return api.post(CREATE_PATH, createRequest, CreateResponse.class);
        }

        private void createBlobs(ModelFile modelFile) {
            createBlob(modelFile.from());
            createBlob(modelFile.adapter());

        }

        private void createBlob(String blobPath) {
            if (blobPath != null) {
                Path path = Paths.get(blobPath);
                if (Files.exists(path)) {
                    String digest = DigestUtils.sha256hex(path);
                    if (blobs().exists(digest) == 404) {
                        int status = blobs().create(path);
                        if (status != 201) {
                            throw new JOllamaClientException("Failed to create blob: " + blobPath);
                        }
                    }

                }
            }
        }

    }

    private class DefaultPullSpec implements PullSpec {

        private final JOllamaApi api;
        private final String name;
        private Boolean insecure;

        public DefaultPullSpec(JOllamaApi api, String name) {
            this.api = api;
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
            return api.stream(PULL_PATH, pullRequest, PullResponse.class);
        }

        @Override
        public PullResponse batch() throws JOllamaClientException {
            return api.post(PULL_PATH, new PullRequest(name, insecure, false),
                    PullResponse.class);
        }

    }

    private class DefaultBlobsSpec implements BlobsSpec {

        private final JOllamaApi api;

        public DefaultBlobsSpec(JOllamaApi api) {
            this.api = api;
        }

        @Override
        public int exists(String digest) throws JOllamaClientException {
            Objects.requireNonNull(digest, "digest must not be null");
            if (!digest.startsWith(DigestUtils.SHA256_PREFIX)) {
                throw new IllegalArgumentException("Invalid digest: " + digest);
            }
            Response response = api.head(BLOBS_PATH + "/" + digest);
            return response.code();
        }

        @Override
        public int create(Path path) throws JOllamaClientException {
            Objects.requireNonNull(path, "path must not be null");
            String digest = DigestUtils.sha256hex(path);
            int exists = exists(digest);
            if (exists == 404) {
                return api.upload(BLOBS_PATH + "/" + digest, path).code();
            }

            return exists;

        }

    }

}
