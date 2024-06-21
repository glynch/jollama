package io.github.glynch.jollama.copy;

/**
 * Copy request.
 * 
 * @param source      The source model name.
 * @param destination The destination model name.
 *
 */
public record CopyRequest(String source, String destination) {
}
