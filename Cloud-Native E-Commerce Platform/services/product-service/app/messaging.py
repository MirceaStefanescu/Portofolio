import json
import logging
import os
from typing import Any, Dict, Optional

from kafka import KafkaProducer

LOGGER = logging.getLogger(__name__)


def kafka_enabled() -> bool:
    return os.getenv("KAFKA_ENABLED", "true").lower() == "true"


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
