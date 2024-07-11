package io.github.glynch.jollama.client;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import io.github.glynch.jollama.Format;
import io.github.glynch.jollama.KeepAlive;
import io.github.glynch.jollama.Model;
import io.github.glynch.jollama.Options;
import io.github.glynch.jollama.chat.ChatResponse;
import io.github.glynch.jollama.chat.Message;
import io.github.glynch.jollama.chat.history.MessageHistory;
import io.github.glynch.jollama.create.CreateResponse;
import io.github.glynch.jollama.embeddings.EmbeddingsResponse;
import io.github.glynch.jollama.generate.GenerateResponse;
import io.github.glynch.jollama.list.ListModel;
import io.github.glynch.jollama.list.ListModels;
import io.github.glynch.jollama.modelfile.InvalidModelFileException;
import io.github.glynch.jollama.modelfile.ModelFile;
import io.github.glynch.jollama.process.ProcessModel;
import io.github.glynch.jollama.process.ProcessModels;
import io.github.glynch.jollama.pull.PullResponse;
import io.github.glynch.jollama.show.ShowResponse;
import reactor.core.publisher.Flux;

/**
 * Fluent Client for the Ollama API.
 * 
 * <p>
 * Use static factory methods {@link #create()} or {@link #create(String)}
 * or {@link JOllamaClient#builder()} or {@link JOllamaClient#builder(String)}
 * to create a new JOllama client.
 * </p>
 * 
 * @author Graham Lynch
 * @see <a href="https://github.com/ollama/ollama/blob/main/docs/api.md">Ollama
 *      API</a>
 * 
 */
public interface JOllamaClient {
    /**
     * The default Ollama host.
     */
    String DEFAULT_OLLAMA_HOST = "http://localhost:11434";

    static String getOllamaHost() {
        String ollamaHost = System.getenv("OLLAMA_HOST");
        if (ollamaHost != null && !ollamaHost.isEmpty()) {
            return ollamaHost;
        }
        return DEFAULT_OLLAMA_HOST;
    }

    /**
     * Create a new {@link JOllamaClient} with the default host of
     * {@link #DEFAULT_OLLAMA_HOST}.
     * 
     * @see #create(String)
     */
    static JOllamaClient create() {
        return builder().build();
    }

    /**
     * A variant of {@link JOllamaClient#create()} that accepts a host.
     * 
     * @param host the Ollama server host for all requests
     * @see #builder(String)
     * 
     * @return a new {@link JOllamaClient}
     */
    static JOllamaClient create(String host) {
        return builder(host).build();
    }

    /**
     * Obtain a builder with the default host.
     * 
     * @return a new {@link JOllamaClient.Builder builder}
     */
    static Builder builder() {
        return new DefaultJOllamaClientBuilder(getOllamaHost());
    }

    /**
     * Obtain a builder with the specified host.
     * 
     * @param host the Ollama server host
     * @return a new {@link JOllamaClient.Builder builder}
     */
    static Builder builder(String host) {
        return new DefaultJOllamaClientBuilder(host);
    }

    /**
     * A mutable builder for creating a {@link JOllamaClient}.
     */
    interface Builder {

        /**
         * Log request and response information.
         * 
         * @return this builder
         */
        Builder log();

        /**
         * Log verbose request and response information.
         * 
         * @return this builder
         */
        Builder verbose();

        /**
         * follow redirects except https to http or http to https
         * 
         * @return this builder
         * 
         * @see Redirect#NORMAL
         */
        Builder followRedirects();

        /**
         * Never follow redirects
         * 
         * @return this builder
         * 
         * @see Redirect#NEVER
         */
        Builder followRedirectsNever();

        /**
         * Always follow redirects
         * 
         * @return this builder
         * 
         * @see Redirect#ALWAYS
         */
        Builder followRedirectsAlways();

        /**
         * Set the connect timeout.
         * 
         * @param duration the duration
         * @return this builder
         */
        Builder connectTimeout(Duration duration);

        /**
         * Set the read timeout.
         * 
         * @param duration the duration
         * @return this builder
         */
        Builder readTimeout(Duration duration);

        JOllamaClient build();

    }

    /**
     * Get the host. Defaults to {@link #DEFAULT_OLLAMA_HOST}.
     * 
     * @return the host
     */
    String getHost();

    /**
     * Get the redirect.
     * 
     * @return the {@link Redirect redirect}
     */
    Redirect getRedirect();

    /**
     * Get the connect timeout.
     * 
     * @return The connect timeout in milliseconds.
     */
    int getConnectTimeout();

    /**
     * Get the read timeout.
     * 
     * @return The read timeout in milliseconds.
     */
    int getReadTimeout();

    /**
     * Get a list of the running models
     * 
     * @see <a href=
     *      "https://github.com/ollama/ollama/blob/main/docs/api.md#list-running-models">List
     *      Running
     *      Models</a>
     * 
     * @return a list of the running models
     * @throws JOllamaClientException in case of request or response errors
     */
    ProcessModels ps() throws JOllamaClientException;

    /**
     * Get the running model with the specified name.
     * 
     * @param name the name of the model
     * @return an {@link Optional} running model with the specified name
     * @throws JOllamaClientException in case of request or response errors
     */

    Optional<ProcessModel> ps(String name) throws JOllamaClientException;

    /**
     * Load the model with the specified name.
     * 
     * @param model the name of the model. This should include the tag. If there is
     *              no tag it defaults to latest
     * @return an {@link Optional} for loaded model with the specified name
     * @throws JOllamaClientException in case of request or response errors
     */
    Optional<ProcessModel> load(String model) throws JOllamaClientException;

    /**
     * Load the model with the specified name.
     * 
     * @param model     then name of the model.
     * @param keepAlive the keep alive {@link KeepAlive}
     * @return an {@link Optional} for loaded model with the specified name
     * @throws JOllamaClientException in case of request or response errors
     */
    Optional<ProcessModel> load(String model, KeepAlive keepAlive) throws JOllamaClientException;

    /**
     *
     * Load the model with the specified name.
     * 
     * @param model the model {@link Model}
     * @return the loaded model
     * @throws JOllamaClientException in case of request or response errors
     */
    Optional<ProcessModel> load(Model model) throws JOllamaClientException;

    /**
     * Load the model with the specified name.
     * 
     * @param model     the model {@link Model}
     * @param keepAlive the keep alive {@link KeepAlive}
     * @return the loaded model
     * @throws JOllamaClientException in case of request or response errors
     */
    Optional<ProcessModel> load(Model model, KeepAlive keepAlive) throws JOllamaClientException;

    /**
     * Ping the host
     * 
     * @return <code>true</code> if the host is reachable, <code>false</code>
     *         otherwise.
     */
    boolean ping();

    /**
     * List the models.
     * 
     * @return a list of the models
     * @throws JOllamaClientException in case of request or response errors
     */
    ListModels list() throws JOllamaClientException;

    /**
     * List the model with the specified name.
     * 
     * @param name the name of the model
     * @return the model with the specified name
     * @throws JOllamaClientException in case of request or response errors
     */
    Optional<ListModel> list(String name) throws JOllamaClientException;

    /**
     * List the model with the specified name.
     * 
     * @param model the {@link Model} to list
     * @return an {@link Optional} for the model with the specified name
     * @throws JOllamaClientException in case of request or response errors
     */
    Optional<ListModel> list(Model model) throws JOllamaClientException;

    /**
     * A builder for a blobs request.
     * 
     * @return a {@link BlobsSpec}
     */
    BlobsSpec blobs();

    /**
     * Show the model with the specified name.
     * 
     * @param name the name of the model
     * @return a {@link ShowResponse} for the model
     * @throws JOllamaClientException in case of request or response errors
     */
    ShowResponse show(String name) throws JOllamaClientException;

    /**
     * Show the model with the specified name.
     * 
     * @param name    the name of the model
     * @param verbose whether to show verbose information
     * @return a {@link ShowResponse} for the model
     * @throws JOllamaClientException in case of request or response errors
     */
    ShowResponse show(String name, boolean verbose) throws JOllamaClientException;

    /**
     * Show the {@code Model model} with the specified name.
     * 
     * @param model the {@link Model model} to show
     * @return a {@link ShowResponse} for the model
     * @throws JOllamaClientException in case of request or response errors
     */
    ShowResponse show(Model model) throws JOllamaClientException;

    /**
     * Show the {@code Model model} with the specified name.
     * 
     * @param model   the {@link Model model} to show
     * @param verbose whether to show verbose information
     * @return a {@link ShowResponse} for the model
     * @throws JOllamaClientException in case of request or response errors
     */
    ShowResponse show(Model model, boolean verbose) throws JOllamaClientException;

    GenerateSpec generate(String model, String prompt);

    GenerateSpec generate(Model model, String prompt);

    /**
     * 
     * Obtains a builder for a chat request.
     * 
     * @param model  The {@link Model} to use
     * @param prompt The user prompt
     * @return a {@link ChatSpec}
     */
    ChatSpec chat(String model, String prompt);

    ChatSpec chat(Model model, String prompt);

    ChatSpec chat(String model, String prompt, String... images);

    ChatSpec chat(Model model, String prompt, String... images);

    ChatSpec chat(String model, String prompt, List<String> images);

    ChatSpec chat(Model model, String prompt, List<String> images);

    /**
     * Obtains a builder for an embeddings request.
     * 
     * @param model  The model to use
     * @param prompt The prompt
     * @return a {@link EmbeddingsSpec}
     */
    EmbeddingsSpec embeddings(String model, String prompt);

    EmbeddingsSpec embeddings(Model model, String prompt);

    CreateSpec create(String name, Path path) throws InvalidModelFileException, IOException;

    CreateSpec create(String name, ModelFile modelFile);

    PullSpec pull(String name);

    PullSpec pull(Model name);

    /**
     * 
     * Copies the source model to the destination model.
     * 
     * @param source      the source model name to copy
     * @param destination the destination model name
     * @return the HTTP status code
     * @throws JOllamaClientException in case of request or response errors
     */
    int copy(String source, String destination) throws JOllamaClientException;

    /**
     * 
     * Copies the source model to the destination model.
     * 
     * @param source      the source model {@link Model} to copy
     * @param destination the destination model name
     * @return the HTTP status code
     * @throws JOllamaClientException in case of request or response errors
     */
    int copy(Model source, String destination) throws JOllamaClientException;

    /**
     * Deletes the model with the specified name.
     * 
     * @param name the name of the model to delete
     * @return the HTTP status code
     * @throws JOllamaClientException in case of request or response errors
     */
    int delete(String name) throws JOllamaClientException;

    /**
     * Deletes the specific {@link Model}
     * 
     * @param name the model {@link io.github.glynch.jollama.Model} to delete
     * @return the HTTP status code
     * @throws JOllamaClientException in case of request or response errors
     */
    int delete(Model name) throws JOllamaClientException;

    /**
     * A builder for a blobs request.
     */
    interface BlobsSpec {
        int exists(String digest) throws JOllamaClientException;

        int create(Path path) throws JOllamaClientException;
    }

    /**
     * A builder for a generate request.
     */
    interface GenerateSpec {

        GenerateSpec images(String image, String... images);

        GenerateSpec image(String image);

        /**
         * Set the format of the response.
         * 
         * @param format the format {@link Format}
         * @return this builder
         */
        GenerateSpec format(Format format);

        /**
         * Set the format of the response to {@link Format#JSON json}
         * 
         * @return this bvuilder
         */
        GenerateSpec json();

        /**
         * Set the options for the request
         * 
         * @param options the {@link io.github.glynch.jollama.Options options}
         * @return this builder
         */
        GenerateSpec options(Options options);

        /**
         * Set the system prompt for this builder.
         * 
         * @param system the system prompt
         * @return this builder
         */
        GenerateSpec system(String system);

        /**
         * Set the template for this builder.
         * 
         * @param template the template
         * @return this builder
         */
        GenerateSpec template(String template);

        /**
         * Set the context for this builder.
         * 
         * @param context  the context
         * @param contexts the contexts
         * @return this builder
         */
        GenerateSpec context(int context, int... contexts);

        GenerateSpec raw(boolean raw);

        GenerateSpec raw();

        /**
         * Set the keep alive for this builder.
         * 
         * @param keepAlive the keep alive {@link io.github.glynch.jollama.KeepAlive}
         * @return this builder
         */
        GenerateSpec keepAlive(KeepAlive keepAlive);

        /**
         * Stream the response.
         * 
         * @return a {@link Flux} of {@link GenerateResponse}
         * @throws JOllamaClientException in case of request or response errors
         */
        Flux<GenerateResponse> stream() throws JOllamaClientException;

        GenerateResponse batch() throws JOllamaClientException;

    }

    /**
     * A builder for a chat request.
     */
    interface ChatSpec {
        /**
         * Sets the system prompt for this builder.
         * 
         * @param system the system message
         * @return a {@link ChatSpec}
         */
        ChatSpec system(String system);

        ChatSpec history(MessageHistory history);

        ChatSpec history(List<Message> messages);

        ChatSpec history(Message... messages);

        ChatSpec format(Format format);

        /**
         * Sets the options for this builder.
         * 
         * @param options the options {@link io.github.glynch.jollama.Options}
         * @return a {@link ChatSpec}
         */
        ChatSpec options(Options options);

        /**
         * 
         * Sets the keep alive for this builder.
         * 
         * @param keepAlive the keep alive {@link io.github.glynch.jollama.KeepAlive}
         * @return a {@link ChatSpec}
         */
        ChatSpec keepAlive(KeepAlive keepAlive);

        Flux<ChatResponse> stream() throws JOllamaClientException;

        ChatResponse batch() throws JOllamaClientException;

    }

    /**
     * A builder for an embeddings request.
     */
    interface EmbeddingsSpec {

        EmbeddingsSpec options(Options options);

        EmbeddingsSpec keepAlive(String keepAlive);

        EmbeddingsResponse get() throws JOllamaClientException;
    }

    /**
     * A builder for a pull request.
     */
    interface PullSpec {
        PullSpec insecure(boolean insecure);

        Flux<PullResponse> stream() throws JOllamaClientException;

        PullResponse batch() throws JOllamaClientException;
    }

    /**
     * A builder for a create request.
     */
    interface CreateSpec {
        Flux<CreateResponse> stream() throws JOllamaClientException;

        CreateResponse batch() throws JOllamaClientException;
    }

}
