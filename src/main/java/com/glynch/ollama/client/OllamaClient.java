package com.glynch.ollama.client;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.glynch.ollama.Format;
import com.glynch.ollama.Model;
import com.glynch.ollama.Options;
import com.glynch.ollama.chat.ChatResponse;
import com.glynch.ollama.chat.Message;
import com.glynch.ollama.chat.history.MessageHistory;
import com.glynch.ollama.create.CreateResponse;
import com.glynch.ollama.embeddings.EmbeddingsResponse;
import com.glynch.ollama.generate.GenerateResponse;
import com.glynch.ollama.list.ListModel;
import com.glynch.ollama.list.ListModels;
import com.glynch.ollama.modelfile.InvalidModelFileException;
import com.glynch.ollama.modelfile.ModelFile;
import com.glynch.ollama.process.ProcessModel;
import com.glynch.ollama.process.ProcessModels;
import com.glynch.ollama.pull.PullResponse;

/**
 * Fluent Client for the Ollama API.
 * 
 * Use static factory methods {@link create()} or {@link create(String)} to
 * create a new Ollama client.
 * 
 * @see <a href="https://github.com/ollama/ollama/blob/main/docs/api.md">Ollama
 *      API</a>
 * 
 */
public interface OllamaClient {
    String DEFAULT_OLLAMA_HOST = "http://localhost:11434";

    /**
     * Create a new Ollama client.
     * 
     * @return a new Ollama client
     * @throws OllamaClientException
     */
    static OllamaClient create() {
        return builder().build();
    }

    /**
     * Create a new Ollama client with the specified host.
     * 
     * @param host
     * @return a new Ollama client
     */
    static OllamaClient create(String host) {
        return builder(host).build();
    }

    /**
     * Create a new Ollama client builder with the default host.
     * 
     * @return a new Ollama client builder
     */
    static Builder builder() {
        return new DefaultOllamaClientBuilder(DEFAULT_OLLAMA_HOST);
    }

    /**
     * Create a new Ollama client builder with the specified host.
     * 
     * @param host
     * @return a new Ollama client builder
     */
    static Builder builder(String host) {
        return new DefaultOllamaClientBuilder(host);
    }

    /**
     * The Ollama client builder interface.
     */
    interface Builder {

        Builder followRedirects();

        Builder connectTimeout(Duration duration);

        OllamaClient build();

    }

    /**
     * Get the host.
     * 
     * @return the host
     */
    String getHost();

    /**
     * Get a list of the running models
     * 
     * @see <a href=
     *      "https://github.com/ollama/ollama/blob/main/docs/api.md#list-running-models">List
     *      Running
     *      Models</a>
     * 
     * @return a list of the running models
     * @throws OllamaClientException
     */
    ProcessModels ps() throws OllamaClientException;

    /**
     * Get the running model with the specified name.
     * 
     * @param name
     * @return the running model with the specified name
     * @throws OllamaClientException
     */

    Optional<ProcessModel> ps(String name) throws OllamaClientException;

    /**
     * Load the model with the specified name.
     * 
     * @param model
     * @return the running model with the specified name
     * @throws OllamaClientException
     */
    ProcessModel load(String model) throws OllamaClientException;

    ProcessModel load(Model model) throws OllamaClientException;

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
     * @throws OllamaClientException
     */
    ListModels list() throws OllamaClientException;

    Optional<ListModel> list(String name) throws OllamaClientException;

    BlobsSpec blobs(String digest);

    ModelFile show(String name) throws OllamaClientException, InvalidModelFileException;

    GenerateSpec generate(String model, String prompt);

    ChatSpec chat(String model, String prompt);

    ChatSpec chat(String model, String prompt, String... images);

    ChatSpec chat(String model, String prompt, List<String> images);

    EmbeddingsSpec embeddings(String model, String prompt);

    CreateSpec create(String name, String modelfile) throws InvalidModelFileException;

    CreateSpec create(String name, Path path) throws InvalidModelFileException, IOException;

    CreateSpec create(String name, ModelFile modelFile);

    PullSpec pull(String name);

    int copy(String source, String destination) throws OllamaClientException;

    int delete(String name) throws OllamaClientException;

    interface BlobsSpec {
        int exists() throws OllamaClientException;

        int create() throws OllamaClientException;
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

        GenerateSpec keepAlive(String keepAlive);

        Stream<GenerateResponse> stream() throws OllamaClientException;

        GenerateResponse batch() throws OllamaClientException;

    }

    interface ChatSpec {

        ChatSpec history(Message... messages);

        ChatSpec history(List<Message> messages);

        ChatSpec history(MessageHistory history);

        ChatSpec format(Format format);

        ChatSpec options(Options options);

        ChatSpec keepAlive(String keepAlive);

        Stream<ChatResponse> stream() throws OllamaClientException;

        ChatResponse batch() throws OllamaClientException;

    }

    interface EmbeddingsSpec {

        EmbeddingsSpec options(Options options);

        EmbeddingsSpec keepAlive(String keepAlive);

        EmbeddingsResponse get() throws OllamaClientException;
    }

    interface PullSpec {
        PullSpec insecure(boolean insecure);

        Stream<PullResponse> stream() throws OllamaClientException;

        PullResponse batch() throws OllamaClientException;
    }

    interface CreateSpec {
        Stream<CreateResponse> stream() throws OllamaClientException;

        CreateResponse batch() throws OllamaClientException;
    }

}
