# Contributor Tasks (Good First Issues)

These tasks are small, well-scoped improvements that are safe for first-time contributors. If you pick one, open an issue and reference this file.

## 1) Add Docker Compose health checks
Labels: good first issue, enhancement

Scope:
- Add health checks for product, order, payment services, plus RabbitMQ and Kafka.
- Update `docker-compose.yml` to use `depends_on` with health checks.

Acceptance criteria:
- `docker compose ps` shows healthy containers after startup.
- README notes the health endpoints used by each service.

## 2) Add API example collection
Labels: good first issue, documentation

Scope:
- Create a `docs/api-examples.http` (or Postman collection) with sample product and order flows.
- Include create product, create order, get order status.

Acceptance criteria:
- Examples work against the local stack using the documented ports.
- README links to the new file.

## 3) Add request correlation IDs
Labels: good first issue, observability

Scope:
- Add a request ID middleware in each service.
- Log the request ID and include it in responses.

Acceptance criteria:
- Logs show a stable request ID for a request lifecycle.
- API responses include an `X-Request-Id` header.
