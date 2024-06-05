package com.glynch.ollama.chat;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

// public final class ChatResponse {

//     private String model;
//     @JsonIgnore
//     @JsonProperty("created_at")
//     private OffsetDateTime createdAt;
//     private Message message;
//     @JsonProperty("done_reason")
//     private String doneReason;
//     private boolean done;
//     @JsonProperty("total_duration")
//     private long totalDuration;
//     @JsonProperty("load_duration")
//     private long loadDuration;
//     @JsonProperty("prompt_eval_duration")
//     private long promptEvalDuration;
//     @JsonProperty("eval_count")
//     private long evalCount;
//     @JsonProperty("eval_duration")
//     private long evalDuration;

//     public String getModel() {
//         return model;
//     }

//     public void setModel(String model) {
//         this.model = model;
//     }

//     public OffsetDateTime getCreatedAt() {
//         return createdAt;
//     }

//     public void setCreatedAt(OffsetDateTime createdAt) {
//         this.createdAt = createdAt;
//     }

//     public Message getMessage() {
//         return message;
//     }

//     public void setMessage(Message message) {
//         this.message = message;
//     }

//     public String getDoneReason() {
//         return doneReason;
//     }

//     public void setDoneReason(String doneReason) {
//         this.doneReason = doneReason;
//     }

//     public boolean isDone() {
//         return done;
//     }

//     public void setDone(boolean done) {
//         this.done = done;
//     }

//     public long getTotalDuration() {
//         return totalDuration;
//     }

//     public void setTotalDuration(long totalDuration) {
//         this.totalDuration = totalDuration;
//     }

//     public long getLoadDuration() {
//         return loadDuration;
//     }

//     public void setLoadDuration(long loadDuration) {
//         this.loadDuration = loadDuration;
//     }

//     public long getPromptEvalDuration() {
//         return promptEvalDuration;
//     }

//     public void setPromptEvalDuration(long promptEvalDuration) {
//         this.promptEvalDuration = promptEvalDuration;
//     }

//     public long getEvalCount() {
//         return evalCount;
//     }

//     public void setEvalCount(long evalCount) {
//         this.evalCount = evalCount;
//     }

//     public long getEvalDuration() {
//         return evalDuration;
//     }

//     public void setEvalDuration(long evalDuration) {
//         this.evalDuration = evalDuration;
//     }

// }

public record ChatResponse(
        String model,
        @JsonIgnore @JsonProperty("created_at") OffsetDateTime createdAt,
        Message message,
        @JsonProperty("done_reason") String doneReason,
        boolean done,
        @JsonProperty("total_duration") long totalDuration,
        @JsonProperty("load_duration") long loadDuration,
        @JsonProperty("prompt_eval_duration") long promptEvalDuration,
        @JsonProperty("eval_count") long evalCount,
        @JsonProperty("eval_duration") long evalDuration) {
}