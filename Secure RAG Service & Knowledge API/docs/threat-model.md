# Threat model basics

## Assets
- Document corpus and embeddings
- JWT secrets and issued tokens
- Audit logs for ingestion and query activity
- Vector store snapshots and backups

## Threats
- Prompt injection to exfiltrate sensitive data
- Unauthorized access to query endpoints
- Poisoned documents or malicious ingestion
- Data leakage via logs or metrics

## Mitigations
- JWT + RBAC enforcement for all endpoints
- Audit logging for ingestion and queries
- Input validation and chunking limits
- Retrieval filtering and role-based document access
