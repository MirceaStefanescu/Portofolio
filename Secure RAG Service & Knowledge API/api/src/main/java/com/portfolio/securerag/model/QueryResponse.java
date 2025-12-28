package com.portfolio.securerag.model;

import java.util.List;

public record QueryResponse(
        String answer,
        List<RetrievedChunk> context,
        long retrievalTimeMs
) {
}
