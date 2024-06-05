package com.glynch.ollama.pull;

// public final class PullResponse {

//     private String status;
//     private String digest;
//     private Long total;
//     private Long completed;

//     public PullResponse() {

//     }

//     public String getStatus() {
//         return status;
//     }

//     public void setStatus(String status) {
//         this.status = status;
//     }

//     public String getDigest() {
//         return digest;
//     }

//     public void setDigest(String digest) {
//         this.digest = digest;
//     }

//     public Long getTotal() {
//         return total;
//     }

//     public void setTotal(Long total) {
//         this.total = total;
//     }

//     public Long getCompleted() {
//         return completed;
//     }

//     public void setCompleted(Long completed) {
//         this.completed = completed;
//     }

// }

public record PullResponse(String status, String digest, Long total, Long completed) {
}
