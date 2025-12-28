package com.portfolio.securerag.model;

import java.time.Instant;

public record AuditEvent(
        Instant timestamp,
        String actor,
        String action,
        String resource,
        String status,
        String details
) {
}
