package com.portfolio.pipeline.api.model;

import java.time.Instant;

public record EventRequest(String eventType, Double value, Instant timestamp) {}
