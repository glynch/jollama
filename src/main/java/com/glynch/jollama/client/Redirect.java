package com.glynch.jollama.client;

/**
 * Enumeration of the possible redirect options for
 * {@link JOllamaClient#builder()}.
 * 
 * @author Graham Lynch
 */
public enum Redirect {
    /**
     * Always redirect, ecept for HTTPS to HTTP urls.
     */
    NORMAL,
    /**
     * Never redirect.
     */
    NEVER,
    /**
     * Always redirect.
     */
    ALWAYS

}
