package com.portfolio.securerag.service;

import com.portfolio.securerag.model.RetrievedChunk;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AnswerComposer {

    private final LlmClient llmClient;

    public AnswerComposer(LlmClient llmClient) {
        this.llmClient = llmClient;
    }

    public String compose(String question, List<RetrievedChunk> chunks) {
        return llmClient.generate(question, chunks);
    }
}
