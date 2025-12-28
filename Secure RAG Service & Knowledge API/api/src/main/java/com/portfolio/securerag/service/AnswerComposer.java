package com.portfolio.securerag.service;

import com.portfolio.securerag.model.RetrievedChunk;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AnswerComposer {

    public String compose(String question, List<RetrievedChunk> chunks) {
        if (chunks == null || chunks.isEmpty()) {
            return "No relevant content found. Ingest documentation and try again.";
        }

        RetrievedChunk top = chunks.get(0);
        String snippet = top.text();
        if (snippet.length() > 400) {
            snippet = snippet.substring(0, 400) + "...";
        }

        return "Based on the retrieved documentation, the most relevant passage is:\n\n"
                + snippet;
    }
}
