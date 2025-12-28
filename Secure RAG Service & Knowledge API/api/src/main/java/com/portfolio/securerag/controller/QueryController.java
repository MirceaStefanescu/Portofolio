package com.portfolio.securerag.controller;

import com.portfolio.securerag.model.QueryRequest;
import com.portfolio.securerag.model.QueryResponse;
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
public class QueryController {

    private final RagService ragService;

    public QueryController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/query")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public QueryResponse query(@Valid @RequestBody QueryRequest request, Authentication authentication) {
        String actor = authentication != null ? authentication.getName() : "unknown";
        return ragService.query(request, actor);
    }
}
