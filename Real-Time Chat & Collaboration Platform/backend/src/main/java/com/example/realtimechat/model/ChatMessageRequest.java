package com.example.realtimechat.model;

import jakarta.validation.constraints.NotBlank;

public record ChatMessageRequest(String sender, @NotBlank String content) {
}
