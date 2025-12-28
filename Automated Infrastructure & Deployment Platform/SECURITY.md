# Security Policy

This project is a portfolio prototype and is not intended for production use.

## Reporting a Vulnerability
If you discover a security issue, please open a GitHub issue or contact the repository owner privately.

## Security Notes
- Secrets are never committed; use `.env` (see `.env.example`).
- Vault is used for short-lived tokens in runtime workflows.
- Threat model basics live in `docs/threat-model.md`.
