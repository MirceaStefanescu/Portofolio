package com.portfolio.securerag.model;

public record RetrievedChunk(
        String documentId,
        String title,
        String source,
        String text,
        double score
) {
}
