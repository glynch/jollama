package com.glynch.jollama.client;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import com.glynch.jollama.Format;
import com.glynch.jollama.KeepAlive;
import com.glynch.jollama.Model;
import com.glynch.jollama.Options;
import com.glynch.jollama.chat.ChatResponse;
import com.glynch.jollama.chat.Message;
import com.glynch.jollama.chat.history.MessageHistory;
import com.glynch.jollama.create.CreateResponse;
import com.glynch.jollama.embeddings.EmbeddingsResponse;
import com.glynch.jollama.generate.GenerateResponse;
import com.glynch.jollama.list.ListModel;
import com.glynch.jollama.list.ListModels;
import com.glynch.jollama.modelfile.InvalidModelFileException;
import com.glynch.jollama.modelfile.ModelFile;
import com.glynch.jollama.process.ProcessModel;
import com.glynch.jollama.process.ProcessModels;
import com.glynch.jollama.pull.PullResponse;

import reactor.core.publisher.Flux;

/**
 * Fluent Client for the Ollama API.
 * 
 * <p>
 * Use static factory methods {@link #create()} or {@link #create(String)}
 * or {@link JOllamaClient#builder()} or {@link JOllamaClient#builder(String)}
 * to create a new Ollama client.
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

    /**
     * Create a new {@code JOllamaClient} with the default host of
     * {@link #DEFAULT_OLLAMA_HOST}.
     * 
     * @see #create(String)
     */
    static JOllamaClient create() {
        return builder().build();
    }

    /**
     * A variant of {@code JOllamaClient#create()} that accepts a host.
     * 
     * @param host the Ollama server host for all requests
     * @see #builder(String)
     */
    static JOllamaClient create(String host) {
        return builder(host).build();
    }

    /**
     * Obtain a {@code JOllamaClient} builder with the default host.
     * 
     */
    static Builder builder() {
        return new DefaultJOllamaClientBuilder(DEFAULT_OLLAMA_HOST);
    }

    /**
     * Create a new {@code JOllamaClient} with the specified host.
     * 
     * @param host the Ollama server host
     * @return a new Ollama client builder
     */
    static Builder builder(String host) {
        return new DefaultJOllamaClientBuilder(host);
    }

    /**
     * A mutable builder for creating a {@link JOllamaClient}.
     */
    interface Builder {

        Builder followRedirects();

        Builder connectTimeout(Duration duration);

        JOllamaClient build();

    }

    /**
     * Get the host. Defaults to {@link #DEFAULT_OLLAMA_HOST}.
     * 
     * @return the host
     */
    String getHost();

    Optional<Duration> getConnectTimeout();

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
     * @return the running model with the specified name
     * @throws JOllamaClientException
     */

    Optional<ProcessModel> ps(String name) throws JOllamaClientException;

    /**
     * Load the model with the specified name.
     * 
     * @param model the name of the model. This should include the tag. If there is
     *              no tag it defaults to latest
     * @return the running model with the specified name
     * @throws JOllamaClientException
     */
    Optional<ProcessModel> load(String model) throws JOllamaClientException;

    Optional<ProcessModel> load(String model, KeepAlive keepAlive) throws JOllamaClientException;

    /**
     * 
     * @param model the model {@link com.glynch.jollama.Model}
     * @return
     * @throws JOllamaClientException
     */
    Optional<ProcessModel> load(Model model) throws JOllamaClientException;

    Optional<ProcessModel> load(Model model, KeepAlive keepAlive) throws JOllamaClientException;

    /**
     * Ping the host
     * 
     * @return true if the host is reachable, false otherwise
     */
    boolean ping();

    /**
     * List the models.
     * 
     * @return a list of the models
     * @throws JOllamaClientException
     */
    ListModels list() throws JOllamaClientException;

    /**
     * 
     * @param name the name of the model
     * @return
     * @throws JOllamaClientException
     */
    Optional<ListModel> list(String name) throws JOllamaClientException;

    Optional<ListModel> list(Model name) throws JOllamaClientException;

    BlobsSpec blobs(String digest);

    ModelFile show(String name) throws JOllamaClientException, InvalidModelFileException;

    ModelFile show(Model name) throws JOllamaClientException, InvalidModelFileException;

    GenerateSpec generate(String model, String prompt);

    /**
     * 
     * @param model  @see {@link com.glynch.jollama.Model}
     * @param prompt @see {@link com.glynch.jollama.chat.Message}
     * @return a {@link ChatSpec}
     */
    ChatSpec chat(String model, String prompt);

    ChatSpec chat(Model model, String prompt);

    ChatSpec chat(String model, String prompt, String... images);

    ChatSpec chat(Model model, String prompt, String... images);

    ChatSpec chat(String model, String prompt, List<String> images);

    ChatSpec chat(Model model, String prompt, List<String> images);

    EmbeddingsSpec embeddings(String model, String prompt);

    EmbeddingsSpec embeddings(Model model, String prompt);

    CreateSpec create(String name, String modelfile) throws InvalidModelFileException;

    CreateSpec create(String name, Path path) throws InvalidModelFileException, IOException;

    CreateSpec create(String name, ModelFile modelFile);

    PullSpec pull(String name);

    PullSpec pull(Model name);

    /**
     * 
     * @param source      the source model name to copy
     * @param destination the destination model name
     * @return the HTTP status code
     * @throws JOllamaClientException
     */
    int copy(String source, String destination) throws JOllamaClientException;

    /**
     * 
     * @param source      the source model {@link com.glynch.jollama.Model} to copy
     * @param destination the destination model name
     * @return the HTTP status code
     * @throws JOllamaClientException
     */
    int copy(Model source, String destination) throws JOllamaClientException;

    /**
     * 
     * @param name the name of the model to delete
     * @return the HTTP status code
     * @throws JOllamaClientException
     */
    int delete(String name) throws JOllamaClientException;

    /**
     * 
     * @param name the model {@link com.glynch.jollama.Model} to delete
     * @return the HTTP status code
     * @throws JOllamaClientException
     */
    int delete(Model name) throws JOllamaClientException;

    interface BlobsSpec {
        int exists() throws JOllamaClientException;

        int create() throws JOllamaClientException;
    }

    interface GenerateSpec {

        GenerateSpec images(String image, String... images);

        GenerateSpec image(String image);

        GenerateSpec format(Format format);

        GenerateSpec json();

        GenerateSpec options(Options options);

        GenerateSpec system(String system);

        GenerateSpec template(String template);

        GenerateSpec context(int context, int... contexts);

        GenerateSpec raw(boolean raw);

        GenerateSpec raw();

        GenerateSpec keepAlive(KeepAlive keepAlive);

        Flux<GenerateResponse> stream() throws JOllamaClientException;

        GenerateResponse batch() throws JOllamaClientException;

    }

    interface ChatSpec {

        /**
         * 
         * @param system the system {@link com.glynch.jollama.chat.Message}
         * @return
         */
        ChatSpec system(String system);

        ChatSpec history(Message... messages);

        ChatSpec history(List<Message> messages);

        ChatSpec history(MessageHistory history);

        ChatSpec format(Format format);

        /**
         * 
         * @param options the options {@link com.glynch.jollama.Options}
         * @return a {@link ChatSpec}
         */
        ChatSpec options(Options options);

        /**
         * 
         * @param keepAlive the keep alive {@link com.glynch.jollama.KeepAlive}
         * @return a {@link ChatSpec}
         */
        ChatSpec keepAlive(KeepAlive keepAlive);

        Flux<ChatResponse> stream() throws JOllamaClientException;

        ChatResponse batch() throws JOllamaClientException;

    }

    interface EmbeddingsSpec {

        EmbeddingsSpec options(Options options);

        EmbeddingsSpec keepAlive(String keepAlive);

        EmbeddingsResponse get() throws JOllamaClientException;
    }

    interface PullSpec {
        PullSpec insecure(boolean insecure);

        Flux<PullResponse> stream() throws JOllamaClientException;

        PullResponse batch() throws JOllamaClientException;
    }

    interface CreateSpec {
        Flux<CreateResponse> stream() throws JOllamaClientException;

        CreateResponse batch() throws JOllamaClientException;
    }

}
