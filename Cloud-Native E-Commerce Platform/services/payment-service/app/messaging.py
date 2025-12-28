import json
import logging
import os
import threading
import time
from typing import Any, Callable, Dict, Optional

import pika
from kafka import KafkaProducer

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


def start_payment_request_consumer(
    on_message: Callable[[Dict[str, Any]], None],
    stop_event: threading.Event,
) -> Optional[threading.Thread]:
    if not rabbitmq_enabled():
        return None

    thread = threading.Thread(
        target=_rabbit_loop,
        args=(on_message, stop_event),
        daemon=True,
    )
    thread.start()
    return thread


def _rabbit_loop(on_message: Callable[[Dict[str, Any]], None], stop_event: threading.Event) -> None:
    url = os.getenv("RABBITMQ_URL", "amqp://guest:guest@rabbitmq:5672/")
    queue = os.getenv("PAYMENT_QUEUE", "payment.requests")

    while not stop_event.is_set():
        try:
            params = pika.URLParameters(url)
            connection = pika.BlockingConnection(params)
            channel = connection.channel()
            channel.queue_declare(queue=queue, durable=True)
            channel.basic_qos(prefetch_count=1)

            def callback(ch, method, properties, body):
                try:
                    payload = json.loads(body.decode("utf-8"))
                    on_message(payload)
                    ch.basic_ack(delivery_tag=method.delivery_tag)
                except Exception as exc:  # pragma: no cover - best effort
                    LOGGER.warning("Failed to process payment request: %s", exc)
                    ch.basic_nack(delivery_tag=method.delivery_tag, requeue=True)

            channel.basic_consume(queue=queue, on_message_callback=callback)

            while not stop_event.is_set():
                connection.process_data_events(time_limit=1)

            connection.close()
        except Exception as exc:  # pragma: no cover - best effort
            LOGGER.warning("RabbitMQ consumer error: %s", exc)
            time.sleep(5)
