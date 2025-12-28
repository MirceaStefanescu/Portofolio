# Real-Time Chat & Collaboration Platform

Developed a real-time chat and collaboration platform using Spring Boot with WebSocket/STOMP backend and Angular front-end with RxJS. Implemented PostgreSQL persistence and Kafka for scalability, containerized with Docker and deployed on Kubernetes via Terraform. Built CI/CD pipelines with Jenkins and integrated OAuth2 authentication along with Grafana and Prometheus for observability.

## Features
- Real-time messaging with WebSocket/STOMP and room subscriptions
- Kafka-backed message fan-out for horizontal scaling
- PostgreSQL persistence with message history and room management
- OAuth2 login (GitHub example) with session-based security
- Prometheus metrics and Grafana dashboards
- Docker Compose for local dev and Terraform for Kubernetes deployments

## Skills and Deliverables
- Spring Boot
- Angular
- Apache Kafka
- Jenkins
- Kubernetes

## Quick Start (Docker)
1. Copy `.env.example` to `.env` and set OAuth2 credentials.
2. Ensure the GitHub OAuth callback is `http://localhost:4201/login/oauth2/code/github`.
3. Run `docker compose -f infra/docker-compose.yml up --build`.
4. Open `http://localhost:4201` and sign in with GitHub (backend is on `http://localhost:8081`).

## Local Development
- Backend: `cd backend` then `mvn spring-boot:run`
- Frontend: `cd frontend` then `npm install` and `npm start`
- Infrastructure: `docker compose -f infra/docker-compose.yml up -d postgres kafka`

## OAuth2
- Create a GitHub OAuth app with callback URL `http://localhost:4201/login/oauth2/code/github` for Docker Compose.
- For local dev (backend only), use `http://localhost:8080/login/oauth2/code/github`.
- Set `GITHUB_CLIENT_ID` and `GITHUB_CLIENT_SECRET` in `.env` or your shell.
- REST and WebSocket features require an authenticated session.

## Observability
- Prometheus: `http://localhost:9090`
- Grafana: `http://localhost:3000` (default `admin` / `admin`)
- Backend metrics: `http://localhost:8081/actuator/prometheus` (Docker Compose) or `http://localhost:8080/actuator/prometheus` (local dev)

## Kubernetes via Terraform
- See `infra/terraform/README.md` for applying to an existing cluster.

## Project Structure
- `backend/` Spring Boot service
- `frontend/` Angular client
- `infra/` Docker Compose, Terraform, and observability config
- `ci/` Jenkins pipeline definition
- `docs/` architecture, API, and deployment notes
