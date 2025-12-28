import os

from fastapi.testclient import TestClient

from app.main import app


def test_create_and_get_product(tmp_path, monkeypatch):
    monkeypatch.setenv("DB_PATH", str(tmp_path / "products.db"))
    monkeypatch.setenv("KAFKA_ENABLED", "false")

    client = TestClient(app)
    payload = {
        "name": "Coffee Beans",
        "description": "1kg bag",
        "price_cents": 1899,
        "currency": "usd",
    }

    response = client.post("/products", json=payload)
    assert response.status_code == 201
    product = response.json()
    assert product["currency"] == "USD"

    get_response = client.get(f"/products/{product['id']}")
    assert get_response.status_code == 200
    fetched = get_response.json()
    assert fetched["name"] == payload["name"]
