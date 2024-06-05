package com.glynch;

import com.glynch.ollama.chat.Message;
import com.glynch.ollama.client.OllamaClient;

public class Main {

    public static void main(String[] args) throws Exception {

        OllamaClient client = OllamaClient.create();

        Message message = Message.user("What is a tsunami?");

        client.chat("llava", message).execute().forEach(response -> {
            System.out.print(response.message().content());
        });

    }
}