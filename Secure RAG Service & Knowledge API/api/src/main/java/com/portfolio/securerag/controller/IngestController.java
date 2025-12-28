package com.portfolio.securerag.controller;

import com.portfolio.securerag.model.IngestRequest;
import com.portfolio.securerag.model.IngestResponse;
import com.portfolio.securerag.service.RagService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class IngestController {

    private final RagService ragService;

    public IngestController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/ingest")
    @PreAuthorize("hasRole('ADMIN')")
    public IngestResponse ingest(@Valid @RequestBody IngestRequest request, Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "unknown";
        return ragService.ingest(request, actor);
    }
}
