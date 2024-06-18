package com.glynch.jollama.create;

import java.nio.file.Path;

import com.glynch.jollama.modelfile.ModelFile;

/**
 * Create request.
 * 
 * @param name      The name of the model
 * @param modelfile The {@link ModelFile#toString() modelfile} to create the
 *                  model with.
 * @param stream    Whether to stream the response or not.
 * @param path      The path to the model file. (Currently not supported)
 * 
 * @author Graham Lynch
 */
public record CreateRequest(String name, String modelfile, Boolean stream, Path path) {
}
