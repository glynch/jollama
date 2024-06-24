package io.github.glynch.jollama.show;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ModelInfo(@JsonProperty("general.architecture") String generalArchitecture,
        @JsonProperty("general.file_type") Integer generalFileType,
        @JsonProperty("general.parameter_count") Long generalParameterCount,
        @JsonProperty("general.quantization_version") Integer generalQuantizationVersion,
        @JsonProperty("llama.attention.head_count") Integer llamaAttentionHeadCount,
        @JsonProperty("llama.attention.head_count_kv") Integer llamaAttentionHeadCountKv,
        @JsonProperty("llama.attention.layer_norm_rms_epsilon") Float llamaAttentionLayerNormRmsEpsilon,
        @JsonProperty("llama.block_count") Integer llamaBlockCount,
        @JsonProperty("llama.contet_length") Integer llamaContetLength,
        @JsonProperty("llama.embedding_length") Integer llamaEmbeddingLength,
        @JsonProperty("llama.feed_forward_length") Integer llamaFeedForwardLength,
        @JsonProperty("llama.rope_dimension_count") Integer llamaRopeDimensionCount,
        @JsonProperty("tokenizer.ggml.bos_token_id") Integer tokenizerGgmlBosTokenId,
        @JsonProperty("tokenizer.ggml.eos_token_id") Integer tokenizerGgmlEosTokenId,
        @JsonProperty("tokenizer.ggml.model") String tokenizerGgmlModel,
        @JsonProperty("tokenizer.ggml.padding_token_id") Integer tokenizerGgmlPaddingTokenId,
        @JsonProperty("tokenizer.ggml.pre") String tokenizerGgmlPre,
        @JsonProperty("tokenizer.ggml.tokens") List<String> tokenizerGgmlTokens,
        @JsonProperty("tokenizer.ggml.token_type") List<Integer> tokenizerGgmlTokenType,
        @JsonProperty("tokenizer.ggml.scores") List<Integer> tokenizerGgmlScores) {

}
