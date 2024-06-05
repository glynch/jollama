package com.glynch;

import com.glynch.ollama.client.OllamaClient;

public class Main {

    public static void main(String[] args) throws Exception {

        OllamaClient client = OllamaClient.create();

        client.embeddings("nomic-embed-text", "What is a Tsunami?").execute().embedding().forEach(System.out::println);

    }
}