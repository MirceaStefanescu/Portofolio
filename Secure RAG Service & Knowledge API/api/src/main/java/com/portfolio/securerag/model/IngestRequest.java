package com.portfolio.securerag.model;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record IngestRequest(
        String documentId,
        @NotBlank String title,
        @NotBlank String content,
        String source,
        List<String> tags
) {
}
