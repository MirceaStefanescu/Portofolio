# Agent Instructions

Before starting any task or step, read this file.
This file is intentionally ignored by git. Keep local-only instructions here.

## Portfolio Organization and GitHub Mirroring

Goal: keep this portfolio hiring-ready and easy to scan in 60 seconds.

### Profile level (GitHub)
- Pin 3 to 6 flagship repos that match the target roles.
- Maintain a profile README with: target roles, core stack, links to pinned repos, contact info.

### Gold standard for each flagship repo
README must include, in this order:
1) One-line summary and target audience
2) Key features (3 to 6 bullets)
3) Tech stack (and why)
4) Demo: video or screenshots, plus live link if possible
5) Quickstart: copy/paste commands
6) Architecture: diagram + explanation
7) Tests: how to run
8) Security notes: secrets, auth, threat model basics
9) Roadmap / tradeoffs

### One-command local run
Prefer one of:
- make dev / make test
- docker compose up
- npm install && npm run dev
- poetry install && pytest

### CI (GitHub Actions)
Minimum:
- lint/format check
- unit tests
- build step
Bonus:
- docker image build
- preview deploy

### Security hygiene
- Never commit secrets; include .env.example.
- Enable secret scanning and push protection.
- Enable Dependabot alerts/updates.
- Optional: CodeQL/code scanning.

### Healthy project files
For flagship repos, include:
- LICENSE
- CONTRIBUTING.md
- CODE_OF_CONDUCT.md

### Track-specific extras
AI-adjacent:
- evals (small test set + metrics), failure cases
- guardrails, prompt injection notes
- cost controls (caching, batching, timeouts)
- observability (logs, traces, dashboards)

Cloud/platform:
- Docker + Compose
- Kubernetes manifests or Helm
- Terraform/IaC for deployment
- health checks, retries, graceful shutdown

Security:
- threat model section in README
- secure auth patterns and input validation
- logging and monitoring notes

### Common mistakes to avoid
- No README, no demo, no quickstart
- Projects that do not run easily
- No tests or CI
- Secrets committed
- Mostly forks/tutorials without your work explained

### Pass/fail checklist for flagship repos
- Clear README + quickstart
- Demo media
- Tests + CI on push/PR
- No secrets, secret scanning enabled
- Dependabot enabled
- Optional: CodeQL

### README template
Use this structure for flagship repos:

# Project Name

One sentence: what it does and who it is for.

## Demo
- Live:
- Video or GIF:
- Screenshots:

## Why this exists
Problem statement, constraints, goals.

## Features
- ...
- ...

## Architecture
Diagram + component and data flow explanation.

## Tech stack
- Backend:
- Frontend:
- Infra:
- CI:

## Quickstart (local)
Prereqs:
- ...

Run:
```
commands
```

## Tests
```
commands
```

## Security
Secrets: use .env (see .env.example). Secret scanning enabled.

## Notes / limitations

## Roadmap / tradeoffs

## Decisions and rationale

### Mirroring to GitHub
- Keep local-only files in local Git excludes (e.g., .git/info/exclude or a global gitignore).
- Commit in logical chunks with clear messages.
- Push after README/CI/quickstart remain accurate.
- Keep GitHub settings enabled: Actions, Dependabot, secret scanning.
