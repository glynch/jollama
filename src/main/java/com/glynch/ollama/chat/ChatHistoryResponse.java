package com.glynch.ollama.chat;

import java.util.List;

public record ChatHistoryResponse(ChatResponse chat, List<Message> history) {

}
