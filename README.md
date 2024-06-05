# Ollama4j Java Library

The Ollama4j Java library provides a fluent Java API for interacting with Ollama

## Usage

### Create an Ollama Client

```java

OllamaClient client = OllamaClient.create();

```

### Get list of models

```java

client.list().forEach(model -> {
    System.out.println(model);
})

```

### Generate (stream)

```java
client.generate("llama3", "What is a tsunami?")
        .stream()
        .execute()
        .forEach(
            r -> {
                System.out.print(r.response());
            });
        System.out.println();
```

### Pull a model

```java
    client.pull("phi3").execute().forEach(r -> {
        System.out.println(r);
    });
```

### Chat

```java

  Message message = Message.user("What is a tsunami?");
        client.chat("llava", message).execute().forEach(response -> {
        System.out.print(response.message().content());
    });
```
