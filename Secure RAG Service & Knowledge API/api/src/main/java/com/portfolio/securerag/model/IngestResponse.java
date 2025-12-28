package com.portfolio.securerag.model;

public record IngestResponse(
        String documentId,
        int chunksIngested,
        String collection
) {
}
