.PHONY: dev test

dev:
	docker compose up --build

test:
	mvn -f services/order-service/pom.xml test
	mvn -f services/inventory-service/pom.xml test
