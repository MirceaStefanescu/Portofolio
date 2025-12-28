package com.example.realtimechat.model;

import java.time.Instant;
import java.util.UUID;

public record ChatRoomResponse(UUID id, String name, Instant createdAt) {
}
