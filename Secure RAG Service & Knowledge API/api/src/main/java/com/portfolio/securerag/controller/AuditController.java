package com.portfolio.securerag.controller;

import com.portfolio.securerag.model.AuditEvent;
import com.portfolio.securerag.service.AuditLogService;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuditController {

    private final AuditLogService auditLogService;

    public AuditController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping("/audit")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuditEvent> audit(@RequestParam(defaultValue = "50") int limit) {
        int safeLimit = limit < 1 ? 1 : limit;
        return auditLogService.list(safeLimit);
    }
}
