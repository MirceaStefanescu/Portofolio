SHELL := /bin/sh
TF_ENV ?= local-kind
TF_DIR := terraform/environments/$(TF_ENV)
IMAGE ?= cicd-demo:local
LOAD_KIND_IMAGE ?= true

.PHONY: dev dev-app test lint ci kind-up kind-down terraform-init terraform-apply terraform-destroy docker-build ansible-config deploy-blue deploy-green deploy-rolling

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

docker-build:
	docker build -t $(IMAGE) .

ansible-config:
	ansible-playbook -i ansible/inventory/local.ini ansible/playbooks/configure-app.yml

deploy-blue:
	IMAGE=$(IMAGE) LOAD_KIND_IMAGE=$(LOAD_KIND_IMAGE) ./scripts/blue-green.sh blue

deploy-green:
	IMAGE=$(IMAGE) LOAD_KIND_IMAGE=$(LOAD_KIND_IMAGE) ./scripts/blue-green.sh green

deploy-rolling:
	IMAGE=$(IMAGE) LOAD_KIND_IMAGE=$(LOAD_KIND_IMAGE) ./scripts/rolling.sh
