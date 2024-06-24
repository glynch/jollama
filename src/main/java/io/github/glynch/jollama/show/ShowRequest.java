package io.github.glynch.jollama.show;

/**
 * Show request.
 * 
 * @param name    The name of the model.
 * @param verbose Whether to show verbose information.
 */
public record ShowRequest(String name, boolean verbose) {
}
