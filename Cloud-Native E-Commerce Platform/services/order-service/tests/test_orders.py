import requests
from fastapi.testclient import TestClient

from app.main import app


def test_create_order(tmp_path, monkeypatch):
    monkeypatch.setenv("DB_PATH", str(tmp_path / "orders.db"))
    monkeypatch.setenv("KAFKA_ENABLED", "false")
    monkeypatch.setenv("RABBITMQ_ENABLED", "false")

    def fake_get(url, timeout=5):
        class Response:
            status_code = 200

            def json(self):
                return {
                    "id": "prod-1",
                    "name": "Coffee",
                    "description": "Beans",
                    "price_cents": 500,
                    "currency": "USD",
                }

        return Response()

    monkeypatch.setattr(requests, "get", fake_get)

    client = TestClient(app)
    payload = {
        "customer_id": "cust-1",
        "items": [{"product_id": "prod-1", "quantity": 2}],
    }

    response = client.post("/orders", json=payload)
    assert response.status_code == 201
    order = response.json()
    assert order["status"] == "pending"
    assert order["total_cents"] == 1000

    get_response = client.get(f"/orders/{order['id']}")
    assert get_response.status_code == 200
    fetched = get_response.json()
    assert fetched["id"] == order["id"]
