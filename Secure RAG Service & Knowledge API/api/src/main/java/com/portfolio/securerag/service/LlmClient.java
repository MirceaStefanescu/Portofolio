package com.portfolio.securerag.service;

import com.portfolio.securerag.model.RetrievedChunk;
import java.util.List;

public interface LlmClient {

    String generate(String question, List<RetrievedChunk> context);
}
