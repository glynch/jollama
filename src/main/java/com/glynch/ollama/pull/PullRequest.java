package com.glynch.ollama.pull;

// public final class PullRequest {

//     private final String name;
//     private final Boolean insecure;
//     private final Boolean stream;

//     public PullRequest(String name, Boolean insecure, Boolean stream) {
//         this.name = name;
//         this.insecure = insecure;
//         this.stream = stream;
//     }

//     public String getName() {
//         return name;
//     }

//     public Boolean isInsecure() {
//         return insecure;
//     }

//     public Boolean isStream() {
//         return stream;
//     }
// }

public record PullRequest(String name, Boolean insecure, Boolean stream) {
}
