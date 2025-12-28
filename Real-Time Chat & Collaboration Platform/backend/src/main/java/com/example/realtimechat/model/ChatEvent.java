package com.example.realtimechat.model;

import java.time.Instant;
import java.util.UUID;

public record ChatEvent(
    UUID messageId,
    UUID roomId,
    String roomName,
    String sender,
    String content,
    Instant createdAt,
    ChatEventType type
) {
}
