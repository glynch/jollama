# Ollama4j Java Library

The Ollama4j Java library provides a fluent Java API for interacting with Ollama REST endpoints.

See [Ollama API](https://github.com/ollama/ollama/blob/main/docs/api.md)

## Usage

### Create an Ollama Client

```java

OllamaClient client = OllamaClient.create();

```

### Ping

Check if Ollama is up

```java

    boolean isUp = client.ping();

```

### Load a model into memory

```java

    boolean isLoaded = client.load("llama3");

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

### Generate (batch)

You can call _batch_ explicitly but it is the default

```java
client.generate("llama3", "What is a tsunami?")
        .batch()
        .execute()
        .forEach(
            r -> {
                System.out.print(r.response());
            });
        System.out.println();
```

### Pull a model

```java
    client.pull("phi3").execute().forEach(response -> {
        System.out.println(response);
    });
```

### Show modefile

```java
    System.out.println(client.show("llama3"));
```

### Chat

Chat by default will _stream_ the results. If you do not want to stream you can call _batch()_ before executing.

```java

  Message message = Message.user("What is a tsunami?");
        client.chat("llava", message).execute().forEach(response -> {
            System.out.print(response.message().content());
        });
```

### Chat with images

```java

Message message = Message.user("Describe this image?", Image.encode(Path.of("image.jpg")));
        client.chat("llava", message)
        .batch()
        .execute().forEach(r -> {
            System.out.println(r.message().content());
        });
```

### Embeddings

```java
    client.embeddings("nomic-embed-text", "What is a Tsunami?")
        .execute()
        .embedding()
        .forEach(System.out::println);
```
