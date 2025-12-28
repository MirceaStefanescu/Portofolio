import logging
import os
import threading
import uuid
from datetime import datetime, timezone
from typing import List, Optional

from fastapi import FastAPI, HTTPException
from fastapi.responses import Response
from prometheus_client import CONTENT_TYPE_LATEST, Counter, generate_latest

from app.db import get_connection, init_db
from app.messaging import build_producer, publish_event, start_payment_request_consumer
from app.models import Payment, PaymentCreate

logging.basicConfig(level=os.getenv("LOG_LEVEL", "INFO"))

app = FastAPI(title="Payment Service")

PAYMENTS_PROCESSED = Counter("payments_processed_total", "Total payments processed")
PAYMENTS_FAILED = Counter("payments_failed_total", "Total payments failed")


@app.on_event("startup")
def on_startup() -> None:
    init_db()
    app.state.producer = build_producer()
    app.state.kafka_topic = os.getenv("KAFKA_TOPIC_PAYMENTS", "payments")
    app.state.stop_event = threading.Event()
    app.state.consumer_thread = start_payment_request_consumer(
        handle_payment_request,
        app.state.stop_event,
    )


@app.on_event("shutdown")
def on_shutdown() -> None:
    app.state.stop_event.set()
    producer = getattr(app.state, "producer", None)
    if producer:
        producer.flush()
        producer.close()


@app.get("/health")
def health() -> dict:
    return {"status": "ok"}


@app.get("/metrics")
def metrics() -> Response:
    return Response(generate_latest(), media_type=CONTENT_TYPE_LATEST)


def _now() -> str:
    return datetime.now(timezone.utc).isoformat()


def _create_payment(order_id: str, amount_cents: int, currency: str) -> Payment:
    status = "authorized" if amount_cents > 0 else "failed"
    payment_id = str(uuid.uuid4())
    created_at = _now()

    payment = Payment(
        id=payment_id,
        order_id=order_id,
        status=status,
        amount_cents=amount_cents,
        currency=currency.upper(),
        created_at=created_at,
    )

    with get_connection() as conn:
        conn.execute(
            """
            INSERT INTO payments (id, order_id, status, amount_cents, currency, created_at)
            VALUES (?, ?, ?, ?, ?, ?)
            """,
            (
                payment.id,
                payment.order_id,
                payment.status,
                payment.amount_cents,
                payment.currency,
                payment.created_at,
            ),
        )
        conn.commit()

    if status == "authorized":
        PAYMENTS_PROCESSED.inc()
    else:
        PAYMENTS_FAILED.inc()

    publish_event(
        app.state.producer,
        app.state.kafka_topic,
        f"payment.{status}",
        payment.model_dump(),
    )
    return payment


def handle_payment_request(message: dict) -> None:
    order_id = message.get("order_id")
    amount_cents = message.get("amount_cents")
    currency = message.get("currency")

    if not order_id or amount_cents is None or not currency:
        return

    _create_payment(order_id, int(amount_cents), currency)


@app.get("/payments", response_model=List[Payment])
def list_payments(order_id: Optional[str] = None) -> List[Payment]:
    query = "SELECT * FROM payments"
    params = ()
    if order_id:
        query += " WHERE order_id = ?"
        params = (order_id,)
    query += " ORDER BY created_at DESC"

    with get_connection() as conn:
        rows = conn.execute(query, params).fetchall()
        return [Payment(**dict(row)) for row in rows]


@app.get("/payments/{payment_id}", response_model=Payment)
def get_payment(payment_id: str) -> Payment:
    with get_connection() as conn:
        row = conn.execute("SELECT * FROM payments WHERE id = ?", (payment_id,)).fetchone()
        if row is None:
            raise HTTPException(status_code=404, detail="Payment not found")
        return Payment(**dict(row))


@app.post("/payments", response_model=Payment, status_code=201)
def create_payment(payload: PaymentCreate) -> Payment:
    return _create_payment(payload.order_id, payload.amount_cents, payload.currency)
