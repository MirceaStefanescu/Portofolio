from fastapi.testclient import TestClient

from app.main import app


def test_create_payment(tmp_path, monkeypatch):
    monkeypatch.setenv("DB_PATH", str(tmp_path / "payments.db"))
    monkeypatch.setenv("KAFKA_ENABLED", "false")
    monkeypatch.setenv("RABBITMQ_ENABLED", "false")

    client = TestClient(app)
    payload = {"order_id": "order-1", "amount_cents": 1200, "currency": "usd"}

    response = client.post("/payments", json=payload)
    assert response.status_code == 201
    payment = response.json()
    assert payment["status"] == "authorized"

    get_response = client.get(f"/payments/{payment['id']}")
    assert get_response.status_code == 200
    fetched = get_response.json()
    assert fetched["order_id"] == payload["order_id"]
