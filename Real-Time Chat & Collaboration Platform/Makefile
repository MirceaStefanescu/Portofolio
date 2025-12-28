.PHONY: dev down test

dev:
	docker compose -f infra/docker-compose.yml up --build

down:
	docker compose -f infra/docker-compose.yml down

test:
	mvn -f backend/pom.xml test
