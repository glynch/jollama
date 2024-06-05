package com.glynch;

import com.glynch.ollama.client.OllamaClient;

public class Main {

    public static void main(String[] args) throws Exception {

        OllamaClient client = OllamaClient.create();

        // Message message = Message.user("What is a tsunami?");

        // client.chat("llava", message).execute().forEach(response -> {
        // System.out.print(response.message().content());
        // });

        client.generate("llama3", "what is a tsunami?")
                .batch()
                .execute()
                .forEach(response -> {
                    System.out.println(response);
                });

        // client.generate("llama3", "What is a tsunami?")
        // .stream(false)
        // .execute()
        // .findFirst()
        // .isPresent(response -> {
        // System.out.println(response);
        // });
    }
}