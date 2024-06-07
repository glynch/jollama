package com.glynch.ollama.client;

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

    boolean ping() throws OllamaClientException;

    ListModels list() throws OllamaClientException;

    Optional<ListModel> list(String name) throws OllamaClientException;

    boolean load(String model) throws OllamaClientException;

    boolean blobs(String digest) throws OllamaClientException;

    ShowResponse show(String name) throws OllamaClientException;

    GenerateSpec generate(String model, String prompt);

    ChatSpec chat(String model, Message message, Message... messages);

    EmbeddingsSpec embeddings(String model, String prompt);

    Stream<CreateResponse> create(String name, String modelfile) throws OllamaClientException;

    PullSpec pull(String name);

    int copy(String source, String destination) throws OllamaClientException;

    int delete(String name) throws OllamaClientException;

    interface GenerateSpec {

        GenerateSpec images(String image, String... images);

        GenerateSpec image(String image);

        GenerateSpec format(Format format);

        GenerateSpec json();

        GenerateSpec options(Options options);

        GenerateSpec system(String system);

        GenerateSpec template(String template);

        GenerateSpec context(int context, int... contexts);

        GenerateSpec stream(boolean stream);

        GenerateSpec stream();

        GenerateSpec batch();

        GenerateSpec raw(boolean raw);

        GenerateSpec raw();

        GenerateSpec keepAlive(String keepAlive);

        Stream<GenerateResponse> execute() throws OllamaClientException;

        GenerateResponse get() throws OllamaClientException;

    }

    interface ChatSpec {

        ChatSpec message(Message message);

        ChatSpec message(String message);

        ChatSpec format(Format format);

        ChatSpec options(Options options);

        ChatSpec stream(boolean stream);

        ChatSpec batch();

        ChatSpec keepAlive(String keepAlive);

        Stream<ChatResponse> execute() throws OllamaClientException;

        ChatResponse get() throws OllamaClientException;

    }

    interface EmbeddingsSpec {

        EmbeddingsSpec options(Options options);

        EmbeddingsSpec keepAlive(String keepAlive);

        EmbeddingsResponse execute() throws OllamaClientException;
    }

    interface PullSpec {
        PullSpec insecure(boolean insecure);

        PullSpec stream(boolean stream);

        PullSpec batch();

        Stream<PullResponse> execute() throws OllamaClientException;

        PullResponse get() throws OllamaClientException;
    }

}
