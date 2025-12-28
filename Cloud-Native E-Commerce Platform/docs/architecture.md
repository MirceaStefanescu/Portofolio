# Architecture

## Components
- Product service: manages catalog data and publishes product events.
- Order service: validates products, creates orders, and requests payments.
- Payment service: consumes payment requests, records payments, and emits payment events.
- RabbitMQ: command queue for payment processing.
- Kafka: event backbone for order/payment streams.
- Prometheus/Grafana: metrics collection and dashboards.
- Elasticsearch/Kibana: log aggregation and search (optional).

## Data flow
1. Client creates a product via the product service.
2. Client creates an order via the order service.
3. Order service publishes a payment request to RabbitMQ and emits an order event to Kafka.
4. Payment service consumes the request, processes payment, and emits a payment event to Kafka.
5. Order service consumes payment events and updates order status.

## Reliability
- Services use local durable storage (SQLite) for the demo.
- Messaging decouples order creation from payment processing.
- Kafka topics can be replayed to rebuild order/payment state.

## Observability
- Each service exposes `/metrics` for Prometheus scraping.
- Logs are written to stdout for container log collection.
