package com.portfolio.securerag.model;

import java.time.Instant;
import java.util.List;

public record TokenResponse(
        String token,
        Instant expiresAt,
        String username,
        List<String> roles
) {
}
