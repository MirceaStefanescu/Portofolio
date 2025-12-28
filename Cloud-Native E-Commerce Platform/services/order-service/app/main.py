import logging
import os
import threading
import uuid
from datetime import datetime, timezone
from typing import Dict, List

import requests
from fastapi import FastAPI, HTTPException
from fastapi.responses import Response
from prometheus_client import CONTENT_TYPE_LATEST, Counter, generate_latest

from app.db import get_connection, init_db
from app.messaging import (
    build_producer,
    publish_event,
    send_payment_request,
    start_payment_event_consumer,
)
from app.models import Order, OrderCreate, OrderItem

logging.basicConfig(level=os.getenv("LOG_LEVEL", "INFO"))

app = FastAPI(title="Order Service")

ORDERS_CREATED = Counter("orders_created_total", "Total orders created")
ORDERS_PAID = Counter("orders_paid_total", "Total orders paid")
ORDERS_FAILED = Counter("orders_failed_total", "Total orders failed")


@app.on_event("startup")
def on_startup() -> None:
    init_db()
    app.state.producer = build_producer()
    app.state.orders_topic = os.getenv("KAFKA_TOPIC_ORDERS", "orders")
    app.state.stop_event = threading.Event()
    app.state.consumer_thread = start_payment_event_consumer(
        handle_payment_event,
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


def _fetch_product(product_id: str) -> Dict:
    base_url = os.getenv("PRODUCT_SERVICE_URL", "http://product-service:8000")
    response = requests.get(f"{base_url}/products/{product_id}", timeout=5)
    if response.status_code == 404:
        raise HTTPException(status_code=400, detail=f"Product {product_id} not found")
    if response.status_code >= 400:
        raise HTTPException(status_code=502, detail="Product service unavailable")
    return response.json()


def _load_items(order_id: str) -> List[OrderItem]:
    with get_connection() as conn:
        rows = conn.execute(
            "SELECT product_id, name, quantity, price_cents FROM order_items WHERE order_id = ?",
            (order_id,),
        ).fetchall()
        return [OrderItem(**dict(row)) for row in rows]


def _load_order(order_id: str) -> Order:
    with get_connection() as conn:
        row = conn.execute("SELECT * FROM orders WHERE id = ?", (order_id,)).fetchone()
        if row is None:
            raise HTTPException(status_code=404, detail="Order not found")
        items = _load_items(order_id)
        return Order(items=items, **dict(row))


def _update_order_status(order_id: str, status: str) -> None:
    with get_connection() as conn:
        conn.execute(
            "UPDATE orders SET status = ?, updated_at = ? WHERE id = ?",
            (status, _now(), order_id),
        )
        conn.commit()


def handle_payment_event(event: Dict) -> None:
    event_type = event.get("event_type")
    payload = event.get("payload", {})
    order_id = payload.get("order_id")
    if not order_id:
        return

    if event_type == "payment.authorized":
        _update_order_status(order_id, "paid")
        ORDERS_PAID.inc()
    elif event_type == "payment.failed":
        _update_order_status(order_id, "payment_failed")
        ORDERS_FAILED.inc()


@app.get("/orders", response_model=List[Order])
def list_orders() -> List[Order]:
    with get_connection() as conn:
        rows = conn.execute("SELECT * FROM orders ORDER BY created_at DESC").fetchall()
    orders = []
    for row in rows:
        order_id = row["id"]
        items = _load_items(order_id)
        orders.append(Order(items=items, **dict(row)))
    return orders


@app.get("/orders/{order_id}", response_model=Order)
def get_order(order_id: str) -> Order:
    return _load_order(order_id)


@app.post("/orders", response_model=Order, status_code=201)
def create_order(payload: OrderCreate) -> Order:
    if not payload.items:
        raise HTTPException(status_code=400, detail="Order must include at least one item")

    items: List[OrderItem] = []
    total_cents = 0
    currency = None

    for item in payload.items:
        product = _fetch_product(item.product_id)
        product_currency = product["currency"]
        if currency and product_currency != currency:
            raise HTTPException(status_code=400, detail="Mixed currencies are not supported")
        currency = product_currency
        line_total = product["price_cents"] * item.quantity
        total_cents += line_total
        items.append(
            OrderItem(
                product_id=item.product_id,
                name=product["name"],
                quantity=item.quantity,
                price_cents=product["price_cents"],
            )
        )

    order_id = str(uuid.uuid4())
    now = _now()

    with get_connection() as conn:
        conn.execute(
            """
            INSERT INTO orders (id, customer_id, status, total_cents, currency, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """,
            (order_id, payload.customer_id, "pending", total_cents, currency, now, now),
        )
        for item in items:
            conn.execute(
                """
                INSERT INTO order_items (order_id, product_id, name, quantity, price_cents)
                VALUES (?, ?, ?, ?, ?)
                """,
                (order_id, item.product_id, item.name, item.quantity, item.price_cents),
            )
        conn.commit()

    ORDERS_CREATED.inc()

    send_payment_request(
        {
            "order_id": order_id,
            "amount_cents": total_cents,
            "currency": currency,
            "customer_id": payload.customer_id,
        }
    )

    publish_event(
        app.state.producer,
        app.state.orders_topic,
        "order.created",
        {
            "order_id": order_id,
            "customer_id": payload.customer_id,
            "total_cents": total_cents,
            "currency": currency,
            "status": "pending",
        },
    )

    return Order(
        id=order_id,
        customer_id=payload.customer_id,
        status="pending",
        total_cents=total_cents,
        currency=currency,
        items=items,
        created_at=now,
        updated_at=now,
    )
