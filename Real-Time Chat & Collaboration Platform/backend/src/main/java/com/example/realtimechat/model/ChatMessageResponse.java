package com.example.realtimechat.model;

import java.time.Instant;
import java.util.UUID;

public record ChatMessageResponse(
    UUID id,
    UUID roomId,
    String roomName,
    String sender,
    String content,
    Instant createdAt
) {
}
