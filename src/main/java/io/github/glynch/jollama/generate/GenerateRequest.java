package io.github.glynch.jollama.generate;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.glynch.jollama.Format;
import io.github.glynch.jollama.KeepAlive;
import io.github.glynch.jollama.Options;

/**
 * Generate request.
 * 
 * @param model     The name of the model to use.
 * @param prompt    The prompt to use.
 * @param images    The images to use. Base64 encoded.
 * @param format    The {@link Format} to use.
 * @param options   The additional model parameters {@link Options} to use.
 * @param system    The system prompt to use. (overrides the modelfile system
 *                  prompt)
 * @param template  The prompt template to use. (overrides the modelfile
 *                  template)
 * @param context   The context to use. Returned from a previous request. For
 *                  conversational history
 * @param stream    Whether to stream the response.
 * @param raw       If {@code true} no formatting will be applied to the
 *                  prompt, amd no context will be returned.
 * @param keepAlive The time to keep the model loaded. {@link KeepAlive}
 * 
 * @see <a href=
 *      "https://github.com/ollama/ollama/blob/main/docs/api.md#generate-a-completion">Generate
 *      a completion</a>
 */
public record GenerateRequest(
                String model,
                String prompt,
                List<String> images,
                Format format,
                Options options,
                String system,
                String template,
                List<Integer> context,
                Boolean stream,
                Boolean raw,
                @JsonProperty("keep_alive") String keepAlive) {

}
