package com.portfolio.pipeline.api.model;

import java.time.Instant;

public record PredictionResponse(
    String eventType,
    Instant windowStart,
    Instant windowEnd,
    long eventCount,
    double avgValue,
    double predictionScore
) {}
