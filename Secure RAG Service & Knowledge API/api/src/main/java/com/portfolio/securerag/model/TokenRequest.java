package com.portfolio.securerag.model;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record TokenRequest(
        @NotBlank String username,
        List<String> roles
) {
}
