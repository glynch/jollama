package io.github.glynch.jollama.create;

import io.github.glynch.jollama.modelfile.ModelFile;

/**
 * Create request.
 * 
 * @param name      The name of the model
 * @param modelfile The {@link ModelFile#toString() modelfile} to create the
 *                  model with.
 * @param stream    Whether to stream the response or not.
 * 
 * @author Graham Lynch
 */
public record CreateRequest(String name, String modelfile, Boolean stream) {
}
