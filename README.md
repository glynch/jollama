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

```java
    OllamaClient client = OllamaClient
        .builder()
        .followRedirects()
        .connectTimeout(Duration.ofSeconds(5))
        .build();

```

### Ping

Check if Ollama is up.

```java

    boolean isUp = client.ping();

```

### Load

Load a model into memory and returns information about the loaded model.

```java
    ProcessModel processModel = client.load("llama3:latest");
```

```java
    ProcessModel processModel = client.load(LLAMA_3_LATEST);
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

### Prompt

```java
     PromptTemplate template = PromptTemplate.template("What is the capital of {country}?");
     System.out.println(template.format("France"));

      Map<String, Object> arguments = Map.of("country", "France");

     System.out.println(template.format(arguments));
```

### Generate (stream)

```java
 client.generate("llama3", "What is a tsunami?")
                    .stream()
                    .forEach(
                            r -> {
                                System.out.print(r.response());
                            });
            System.out.println();
```

### Generate (batch)

```java
    GenerateResponse generateResponse = client.generate("llama3", "What is a tsunami?")
                    .batch()
                    .get();
    System.out.println(generateResponse.response());
```

### Generate (Options)

This example also gives the same result each time since it uses the same _seed_ and _0_ temperature.

```java
    Options options = Options.builder().temperature(0f).seed(42).build();
    System.out.println(client.generate("llama3", "Why is the sky blue?")
            .options(options)
            .batch());
```

### Pull

Pull a model.

```java
    client.pull("phi3").stream().forEach(response -> {
        System.out.println(response.status());
    });
```

### Pull (batch)

```java

    System.out.println(client.pull("phi3").batch());
```

### Create

Create a model.

```java

 client.create("mario-test", "FROM llama3\nSYSTEM You are mario from Super Mario Bros.")
        .stream().forEach(
            System.out::println
        );
```

```java
    ModelFile modelFile = ModelFile.from("llama3")
            .system("You are mario from Super MarioBros.")
            .temperature(0f)
            .seed(42)
            .build();

    client.create("mario-test", modelFile)
        .stream().forEach(
            System.out::println
        );
```

Read a modefile from disk

```java
    ModelFile modelFile = ModelFile.parse(Path.of("mario-test.modelfile"))
     client.create("mario-test", modelFile)
        .stream().forEach(
            System.out::println
        );
```

### Copy

Copy a model.

```java
    int statusCode = client.copy("llama3", "llama3-copy");

```

### Delete

Delete a model

```java
    int statucCode = client.delete("llama3-copy");

```

### Show

Show the contents of a modelfile for an existing model.

```java
    System.out.println(client.show("llama3"));
```

### Chat

Chat with a model and stream the result.

```java

  client.chat("llama3", "What is a tsunami?").stream().forEach(response -> {
                System.out.print(response.message().content());
            });
```

### Chat (images)

Chat with images. You need to ensure you use a multimodal model such as _llava_.

```java

 System.out.println(client
                    .chat("llava", "Describe this image?",
                            Image.encode(Path.of("image.png")))
                    .batch()
                    .message().content());
```

### Chat (history)

Chat with a model using the history of previous messages.

```java

    MessageHistory history = new InMemoryMessageHistory();
    client.chat("llama3", "Why is the sky blue?").history(history).batch();
    client.chat("llama3", "How is that different than mie scattering?").history(history).batch();

    for (Message message : history) {
        System.out.println(message);
    }
```

### Chat (history, stream)

Since the message is streamed yo0u need to add it to the history manually.

```java
     MessageHistory history = new InMemoryMessageHistory();
     client.chat("llama3", "Why is the sky blue?").history(history).batch();
            StringBuilder content = new StringBuilder();
            client.chat("llama3", "How is that different than mie scattering?")
                    .history(history)
                    .stream()
                    .forEach(
                            r -> {
                                content.append(r.message().content());
                                System.out.print(r.message().content());
                            });

            history.add(Message.assistant(content.toString()));

            for (Message message : history) {
                System.out.println(message);
            }
```

### Embeddings

Generate embeddings for a prompt.

```java
    client.embeddings("nomic-embed-text", "What is a Tsunami?")
        .get()
        .embedding()
        .forEach(System.out::println);
```

### Blob (exists)

Check if a blob exists.

```java
    System.out.println(
            client.blobs("sha256:00e1317cbf74d901080d7100f57580ba8dd8de57203072dc6f668324ba545f29")
            .exists()
            );
```

### Process

Show currently loaded models and associated model information.

```java
    client.ps().models().forEach(System.out::println);
```

```java
    client.ps("llama3:latest").ifPresent(System.out::println);

```
