package io.github.glynch;

import java.util.concurrent.ExecutionException;

import io.github.glynch.jollama.Model;
import io.github.glynch.jollama.client.JOllamaClient;
import io.github.glynch.jollama.modelfile.ModelFile;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        JOllamaClient client = JOllamaClient.create();
        // MessageHistory history = new InMemoryMessageHistory();
        // try {

        // client.chat("llama3", "Why is the sky blue?").system("You are an expert
        // meteorologist")
        // .history(history)
        // .stream().subscribe(
        // r -> System.out.print(r.message().content()));

        // client.chat("llama3", "How is that different than mie
        // scattering?").history(history)
        // .stream().subscribe(
        // r -> System.out.print(r.message().content()));
        // System.out.println();
        // } catch (JOllamaClientResponseException e) {
        // System.out.println(e.getMessage());
        // System.out.println(e.getStatusCode());
        // } catch (JOllamaClientRequestException e) {
        // System.out.println(e.getMessage());
        // System.out.println(e.getUrl());
        // System.out.println(e.getMethod());
        // }

        // for (Message message : history.messages()) {
        // System.out.println(message);
        // }

        ModelFile modelFile = client.show(Model.ORCA_MINI_LATEST);
        System.out.println(modelFile.template());
        // client.pull(Model.ORCA_MINI_LATEST)
        // .stream()
        // .subscribe(
        // r -> System.out.println(r.status()));

    }

}
