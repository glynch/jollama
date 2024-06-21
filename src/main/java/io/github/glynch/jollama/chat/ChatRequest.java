package io.github.glynch.jollama.chat;

import java.util.List;

import io.github.glynch.jollama.Format;
import io.github.glynch.jollama.Options;

/**
 * Chat request.
 * 
 * @param model     (required) The model to use for the chat request.
 * @param messages  The list of messages to chat with.
 * @param format    The format of the chat response. Currently only "json"
 *                  is supported.
 * @param options   The {@link Options} options for the chat request.
 * @param stream    Whether to stream the response or not.
 * @param keepAlive The keep alive value.
 * 
 * @author Graham Lynch
 * @see <a href=
 *      "https://github.com/ollama/ollama/blob/main/docs/api.md#generate-a-chat-completion">Generate
 *      a chat completion</a>
 */
public record ChatRequest(
                String model,
                List<Message> messages,
                Format format,
                Options options,
                Boolean stream,
                String keepAlive) {

}
