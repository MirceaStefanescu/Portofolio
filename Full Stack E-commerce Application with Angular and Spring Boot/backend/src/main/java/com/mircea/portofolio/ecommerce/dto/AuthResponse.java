package com.mircea.portofolio.ecommerce.dto;

import java.time.Instant;

public record AuthResponse(String token, Instant expiresAt, String email) {}
