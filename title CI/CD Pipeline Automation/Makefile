SHELL := /bin/sh
TF_DIR := terraform/environments/local-kind

.PHONY: dev dev-app test lint ci kind-up kind-down terraform-init terraform-apply terraform-destroy ansible-config deploy-blue deploy-green deploy-rolling

dev:
	docker compose up --build

dev-app:
	npm install
	npm run dev

test:
	npm test

lint:
	npm run lint

ci:
	./scripts/ci.sh

kind-up:
	./scripts/kind-up.sh

kind-down:
	./scripts/kind-down.sh

terraform-init:
	terraform -chdir=$(TF_DIR) init

terraform-apply:
	terraform -chdir=$(TF_DIR) apply -auto-approve

terraform-destroy:
	terraform -chdir=$(TF_DIR) destroy -auto-approve

ansible-config:
	ansible-playbook ansible/playbooks/configure-app.yml

deploy-blue:
	./scripts/blue-green.sh blue

deploy-green:
	./scripts/blue-green.sh green

deploy-rolling:
	./scripts/rolling.sh
