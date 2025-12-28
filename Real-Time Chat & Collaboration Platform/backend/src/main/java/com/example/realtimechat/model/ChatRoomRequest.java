package com.example.realtimechat.model;

import jakarta.validation.constraints.NotBlank;

public record ChatRoomRequest(@NotBlank String name) {
}
