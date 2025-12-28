import json
import logging
import os
import threading
import time
from typing import Any, Callable, Dict, Optional

import pika
from kafka import KafkaConsumer, KafkaProducer

LOGGER = logging.getLogger(__name__)


def kafka_enabled() -> bool:
    return os.getenv("KAFKA_ENABLED", "true").lower() == "true"


def rabbitmq_enabled() -> bool:
    return os.getenv("RABBITMQ_ENABLED", "true").lower() == "true"


def build_producer() -> Optional[KafkaProducer]:
    if not kafka_enabled():
        LOGGER.info("Kafka disabled via KAFKA_ENABLED=false")
        return None

    bootstrap = os.getenv("KAFKA_BOOTSTRAP", "kafka:9092")
    return KafkaProducer(
        bootstrap_servers=bootstrap,
        value_serializer=lambda value: json.dumps(value).encode("utf-8"),
        retries=3,
    )


def publish_event(
    producer: Optional[KafkaProducer],
    topic: str,
    event_type: str,
    payload: Dict[str, Any],
) -> None:
    if producer is None:
        return

    event = {"event_type": event_type, "payload": payload}
    try:
        producer.send(topic, event).get(timeout=5)
    except Exception as exc:  # pragma: no cover - best effort
        LOGGER.warning("Failed to publish Kafka event: %s", exc)


def send_payment_request(message: Dict[str, Any]) -> None:
    if not rabbitmq_enabled():
        return

    url = os.getenv("RABBITMQ_URL", "amqp://guest:guest@rabbitmq:5672/")
    queue = os.getenv("PAYMENT_QUEUE", "payment.requests")

    try:
        params = pika.URLParameters(url)
        connection = pika.BlockingConnection(params)
        channel = connection.channel()
        channel.queue_declare(queue=queue, durable=True)
        channel.basic_publish(
            exchange="",
            routing_key=queue,
            body=json.dumps(message),
            properties=pika.BasicProperties(delivery_mode=2),
        )
        connection.close()
    except Exception as exc:  # pragma: no cover - best effort
        LOGGER.warning("Failed to publish RabbitMQ message: %s", exc)


def start_payment_event_consumer(
    on_event: Callable[[Dict[str, Any]], None],
    stop_event: threading.Event,
) -> Optional[threading.Thread]:
    if not kafka_enabled():
        return None

    thread = threading.Thread(
        target=_consumer_loop,
        args=(on_event, stop_event),
        daemon=True,
    )
    thread.start()
    return thread


def _consumer_loop(on_event: Callable[[Dict[str, Any]], None], stop_event: threading.Event) -> None:
    topic = os.getenv("KAFKA_TOPIC_PAYMENTS", "payments")
    bootstrap = os.getenv("KAFKA_BOOTSTRAP", "kafka:9092")
    group_id = os.getenv("KAFKA_GROUP_ID", "order-service")

    while not stop_event.is_set():
        try:
            consumer = KafkaConsumer(
                topic,
                bootstrap_servers=bootstrap,
                group_id=group_id,
                value_deserializer=lambda value: json.loads(value.decode("utf-8")),
                auto_offset_reset="earliest",
                enable_auto_commit=True,
            )
            for message in consumer:
                if stop_event.is_set():
                    break
                on_event(message.value)
            consumer.close()
        except Exception as exc:  # pragma: no cover - best effort
            LOGGER.warning("Kafka consumer error: %s", exc)
            time.sleep(5)
