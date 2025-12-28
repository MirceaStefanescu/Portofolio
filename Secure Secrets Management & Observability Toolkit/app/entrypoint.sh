#!/bin/sh
set -e

if [ -n "$VAULT_ROLE_ID_FILE" ] && [ -n "$VAULT_SECRET_ID_FILE" ]; then
  printf "Waiting for Vault AppRole files...\n"
  while [ ! -f "$VAULT_ROLE_ID_FILE" ] || [ ! -f "$VAULT_SECRET_ID_FILE" ]; do
    sleep 2
  done
fi

exec node src/index.js
