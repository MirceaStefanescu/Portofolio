package com.portfolio.securerag.service;

import java.util.Map;

public record QdrantPoint(
        String id,
        float[] vector,
        Map<String, Object> payload
) {
}
