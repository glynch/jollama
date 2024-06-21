package io.github.glynch.jollama.embeddings;

import java.util.List;

/**
 * Embeddings response.
 * 
 * @param embedding The embeddings.
 *
 * @author Graham Lynch
 */
public record EmbeddingsResponse(List<Double> embedding) {

}