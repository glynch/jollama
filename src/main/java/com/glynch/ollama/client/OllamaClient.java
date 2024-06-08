package com.glynch.ollama.client;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

import com.glynch.ollama.Format;
import com.glynch.ollama.Options;
import com.glynch.ollama.chat.ChatResponse;
import com.glynch.ollama.chat.Message;
import com.glynch.ollama.create.CreateResponse;
import com.glynch.ollama.embeddings.EmbeddingsResponse;
import com.glynch.ollama.generate.GenerateResponse;
import com.glynch.ollama.list.ListModel;
import com.glynch.ollama.list.ListModels;
import com.glynch.ollama.modelfile.ModelFile;
import com.glynch.ollama.process.ProcessModel;
import com.glynch.ollama.process.ProcessModels;
import com.glynch.ollama.pull.PullResponse;
import com.glynch.ollama.show.ShowResponse;

public interface OllamaClient {

    static OllamaClient create() {
        return new DefaultOllamaClient();
    }

    static OllamaClient create(String host) {
        return new DefaultOllamaClient(host);
    }

    String getHost();

    ProcessModels ps() throws OllamaClientException;

    Optional<ProcessModel> ps(String name) throws OllamaClientException;

    void load(String model) throws OllamaClientException;

    boolean ping();

    ListModels list() throws OllamaClientException;

    Optional<ListModel> list(String name) throws OllamaClientException;

    BlobsSpec blobs(String digest);

    ShowResponse show(String name) throws OllamaClientException;

    GenerateSpec generate(String model, String prompt);

    ChatSpec chat(String model, Message message, Message... messages);

    EmbeddingsSpec embeddings(String model, String prompt);

    CreateSpec create(String name, String modelfile);

    CreateSpec create(String name, Path path);

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

        ChatSpec message(Message message);

        ChatSpec message(String message);

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
