.PHONY: dev down logs test

dev:
	docker compose up --build

down:
	docker compose down -v

logs:
	docker compose logs -f --tail=200

test:
	mvn -f api/pom.xml test
	mvn -f flink/pom.xml test
