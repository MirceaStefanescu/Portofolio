.PHONY: dev demo test

dev:
	docker compose up --build

demo:
	./scripts/demo.sh

test:
	mvn -f api/pom.xml test
