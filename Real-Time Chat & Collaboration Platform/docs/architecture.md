# Architecture

## Overview

Browser
  |  HTTP (REST, OAuth2) + WebSocket/STOMP
  v
Angular Frontend
  |  /api/* and /ws
  v
Spring Boot Backend
  |  persist, publish
  v
PostgreSQL <--- Kafka ---> Spring Boot Consumers
  |                    |  broadcast
  v                    v
Message History      WebSocket Topics

Prometheus scrapes /actuator/prometheus and Grafana visualizes metrics.
OAuth2 login establishes a session cookie that is reused for REST and WebSocket traffic.

## Components
- Angular client for rooms, chat, and message history
- Spring Boot service for WebSocket/STOMP (SockJS), REST APIs, and OAuth2 login
- PostgreSQL for room and message persistence
- Kafka for message fan-out and multi-instance scalability
- Prometheus and Grafana for observability

## Scaling Notes
- Each backend instance uses a unique Kafka consumer group (default `realtime-chat-${HOSTNAME}`) so every node broadcasts messages to its connected clients.

## Message Flow
1. Client sends a message to `/app/rooms/{roomId}/send`.
2. Backend persists the message and publishes a `ChatEvent` to Kafka.
3. Kafka consumers broadcast the event to `/topic/rooms/{roomId}`.
4. Clients subscribed to the topic receive the new message in real time.
