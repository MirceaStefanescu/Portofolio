package com.mircea.portofolio.ecommerce.service;

import java.time.Instant;

public record JwtToken(String token, Instant expiresAt) {}
