package com.glynch.ollama.create;

import java.nio.file.Path;

public record CreateRequest(String name, String modelfile, Boolean stream, Path path) {
}
