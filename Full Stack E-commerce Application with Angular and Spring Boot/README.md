# Full Stack E-commerce Application with Angular and Spring Boot

## Overview

Full stack e-commerce application with an Angular front end and Spring Boot back
end. The app includes a product catalog, shopping cart, user authentication,
Stripe payment processing, and MySQL persistence. The UI is responsive and built
with TypeScript and CSS, and the stack is prepared for Docker and Kubernetes
deployment.

## Tech Stack

- Angular (latest)
- Spring Boot 4
- Java 21
- MySQL
- Stripe API
- Docker
- Kubernetes

## Status

- Scaffolded with working API contracts and UI pages

## Structure

- `frontend/` - Angular app
- `backend/` - Spring Boot app
- `docs/` - module-specific docs

## Key Features

- Product catalog with category browsing
- Shopping cart with add/remove/update quantity
- User authentication and protected checkout
- Stripe payment intent creation
- MySQL-backed persistence for products, users, and orders
- Responsive UI optimized for desktop and mobile
- Containerized deployment with Docker and Kubernetes manifests

## Local Development

### Prerequisites

- Node.js 20+ and npm
- Angular CLI
- Java 21
- Maven
- MySQL 8+
- A Stripe account and API keys

### Environment Setup

Create environment files:

- `backend/.env` (copy `backend/.env.example`)

Recommended variables:

- `SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/ecommerce`
- `SPRING_DATASOURCE_USERNAME=ecommerce`
- `SPRING_DATASOURCE_PASSWORD=ecommerce`
- `JWT_SECRET=change_me_to_a_long_random_string_at_least_32_bytes`
- `JWT_TTL_MINUTES=60`
- `STRIPE_SECRET_KEY=sk_test_...`
- `STRIPE_WEBHOOK_SECRET=whsec_...`

Frontend API base URL:

- Update `frontend/src/app/core/config/app-config.ts` to match your backend URL.

### Run Backend

From `backend/`:

```bash
mvn spring-boot:run
```

### Run Frontend

From `frontend/`:

```bash
npm install
npm run start
```

### Run Tests

```bash
# backend
mvn test

# frontend
npm test
```

## API Endpoints

- `GET /api/health`
- `GET /api/products`
- `GET /api/products/{id}`
- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/checkout/payment-intent`

Stripe amount values are expected in the smallest currency unit (e.g., cents).

## Docker

From the module root:

```bash
docker compose up --build
```

## Deployment

- Docker images for frontend and backend
- Kubernetes manifests for deployment, service, and ingress
