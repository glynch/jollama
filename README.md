# Ollama4j Java Library

The Ollama4j Java library provides a fluent Java API for interacting with Ollama REST endpoints.

See [Ollama API](https://github.com/ollama/ollama/blob/main/docs/api.md)

## Usage

### Create an Ollama Client

```java

    OllamaClient client = OllamaClient.create();

```

```java
    OllamaClient client = OllamaClient.create("http://localhost:11434");

```

### Ping

Check if Ollama is up.

```java

    boolean isUp = client.ping();

```

### Load

Load a model into memory.

```java

    boolean isLoaded = client.load("llama3");

```

### List

Get a list of the current models.

```java

    client.list().forEach(model -> {
            System.out.println(model);
    })

```

```java

    client.list("llama3:latest").ifPresent(System.out::println);
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
    GenerateResponse generateResponse = client.generate("llama3", "What is a tsunami?")
                    .batch()
                    .execute()
                    .findFirst().get();
    System.out.println(generateResponse.response());
```

### Generate (Options)

This eample also gives the same result each time since it uses the same _seed_ and _0_ temperature.

```java
    Options options = Options.builder().temperature(0f).seed(42).build();
    client.generate("llama3", "Why is the sky blue?")
            .options(options)
            .batch()
            .execute()
            .forEach(System.out::println);
```

### Pull

Pull a model.

```java
    client.pull("phi3").execute().forEach(response -> {
        System.out.println(response.status());
    });
```

### Pull (batch)

```java

    client.pull("phi3").batch().execute().forEach(response -> {
                System.out.println(response.status());
            });
```

### Create

CReate a model.

### Copy

Copy a model.

```java
    int statusCode = client.copy("llama3", "llama3-copy");

```

### Delete

```java
    int statucCode = client.delete("llama3-copy");

```

### Show

Show the contents of a modelfile for an existing model.

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

### Chat (images)

Chat with images. You need to ensure you use a multimodel model such as _lava_.

```java

Message message = Message.user("Describe this image?", Image.encode(Path.of("image.jpg")));
        client.chat("llava", message)
        .batch()
        .execute().forEach(r -> {
            System.out.println(r.message().content());
        });
```

### Chat (history)

Chat with a model using the hsitory of previous messages.

```java

    Message question = Message.user("Why is the sky blue?");
    Message answer = client.chat("llava", question).batch().execute().findFirst().get().message();
    System.out.println(answer.content())
    System.out.println(client
                .chat("llava", question, answer, Message.user("How is that different than mie scattering?"))
                .batch()
                .execute().findFirst().get().message().content());
```

### Embeddings

```java
    client.embeddings("nomic-embed-text", "What is a Tsunami?")
        .execute()
        .embedding()
        .forEach(System.out::println);
```

### Process

Show currently loaded models and associated model information.

```java
    client.ps().models().forEach(System.out::println);
```

```java
    client.ps("llama3:latest").ifPresent(System.out::println);

```
