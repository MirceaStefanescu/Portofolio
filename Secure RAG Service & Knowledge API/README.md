# Secure RAG Service & Knowledge API

Secure retrieval-augmented generation (RAG) microservice for platform and knowledge teams that need question answering over open-source documentation with high availability.

## Features
- Ingests open-source documentation, chunks content, and embeds it into a replicated vector store.
- Spring Boot API for retrieval and question answering.
- JWT-protected endpoints with role-based access control (RBAC).
- Audit logging for access and inference activity.
- Vector stores replicated across availability zones with Velero backups for disaster recovery.
- New Relic monitoring and Helm-based deployment to multi-cloud Kubernetes clusters.

## Tech stack (and why)
- Spring Boot (Java 17): REST API, dependency injection, and production-ready runtime.
- Spring Security + JWT: authentication, authorization, and RBAC enforcement.
- Qdrant (replicated across AZs): vector store for low-latency semantic retrieval and high availability.
- LangChain4j: RAG orchestration and retrieval workflows in Java.
- Kubernetes + Helm: repeatable, multi-cloud deployments and upgrades (chart in `helm/secure-rag`).
- Velero: backup and restore for stateful vector data.
- New Relic: traces, metrics, and logs for performance and reliability.

## Demo
- Live: TBD
- Video or GIF: `demo/rag-query.svg`
- Screenshots: `demo/rag-overview.svg`

## Quickstart (local)
Prereqs:
- Java 17+
- Docker and Docker Compose
- Python 3 (for the demo script)

Run:
```
docker compose up --build
./scripts/demo.sh
```

Makefile shortcuts:
```
make dev
make demo
```

Local API: `http://localhost:8090`

Config:
- `cp .env.example .env` and adjust secrets for local runs.
- For non-Docker runs, set `QDRANT_URL=http://localhost:6333`.

## Architecture
```mermaid
flowchart LR
  Docs[Open-source docs] --> Ingest[Ingestion + Chunking]
  Ingest --> Embed[Embedding Service]
  Embed --> VStore[(Vector Store Primary)]
  VStore --> Replica[(Vector Store Replica)]
  API[Spring Boot API] --> Retriever[Retriever + Answering]
  Retriever --> VStore
  API --> Auth[JWT + RBAC]
  API --> Audit[Audit Logs]
  VStore --> Backup[Velero Backup]
  API --> NR[New Relic]
  K8s[Kubernetes + Helm] --> API
  K8s --> VStore
```

Docs are ingested, chunked, embedded, and stored in a replicated vector store. The Spring Boot API performs retrieval and answer generation behind JWT and RBAC controls, while audit logs capture access events. Velero protects vector data backups, and New Relic provides telemetry for latency and reliability across multi-cloud Kubernetes clusters.

## Tests
```
mvn -f api/pom.xml test
```

Or:
```
make test
```

## Security
Secrets: use `.env` (see `.env.example`). JWT-based authentication and RBAC restrict access to endpoints; the dev token endpoint is gated by `X-Dev-Key` and should be disabled in production. Threat model basics in `docs/threat-model.md` cover prompt injection and data exfiltration risks. Use Kubernetes RBAC, network policies, and least-privilege service accounts for production.

## Notes / limitations
- Local mode uses deterministic hash-based embeddings to stay self-contained; swap in LangChain4j-backed embeddings/LLMs for production quality.

## Roadmap / tradeoffs
- Add eval sets, prompt-injection tests, and regression metrics for RAG quality.
- Add hybrid search (BM25 + vector) and reranking for higher recall.
- Add caching and batching to control embedding and retrieval costs.
- Tradeoff: multi-AZ replication increases cost and operational overhead in exchange for availability.

## Tags
ai, security, docker, knowledge base, spring boot, jwt, kubernetes, helm, rag, new relic, qdrant, vector store

## Skills and tools
Tools and software: ai, security, docker, knowledge base, spring boot, jwt, kubernetes, helm, rag, new relic, qdrant, vector store, langchain4j.

## Project details
Industry: ai, security, docker, knowledge base, spring boot, rag.
