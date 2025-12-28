# Portal UI

This folder contains the self-service portal UI and API.

## What it does
- Serves the portal UI from `public/`.
- Generates pipeline, Terraform, and Helm templates via the API.
- Renders templates stored in `templates/` with app/environment inputs.
- Exposes a simple Prometheus metrics endpoint.

## Folder layout
- `public/`: UI assets (HTML/CSS/JS).
- `src/`: Express API and template rendering logic.
- `templates/`: pipeline, Terraform, and Helm template files.

## API endpoints
- `GET /api/health`
- `GET /api/services`
- `POST /api/pipeline`
- `POST /api/terraform`
- `POST /api/helm`
- `GET /api/metrics`

Example:
```
curl -X POST http://localhost:8080/api/pipeline \
  -H 'Content-Type: application/json' \
  -d '{"appName":"orders-api","environment":"dev","provider":"github","appCount":3}'
```

## Configuration and logging
- Configure service URLs and log level via `.env` (see `.env.example`).
- Logs are JSON with `requestId` for traceability (see `docs/logging.md`).

## Local dev
The portal runs through Docker Compose at `http://localhost:8080` (Node 18 runtime).
For direct runs:
```
npm install
npm start
```
