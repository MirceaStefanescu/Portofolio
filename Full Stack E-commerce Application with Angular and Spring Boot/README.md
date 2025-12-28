# Full Stack E-commerce Application with Angular and Spring Boot

## Overview

Full stack e-commerce application with an Angular front end and Spring Boot back
end. The app includes a product catalog, shopping cart, user authentication,
Stripe payment processing, and MySQL persistence. The UI is responsive and built
with TypeScript and CSS, and the stack is prepared for Docker and Kubernetes
deployment.

## Tech Stack

- Angular
- Spring Boot
- MySQL
- Stripe API
- Docker
- Kubernetes

## Structure

- `frontend/` - Angular app
- `backend/` - Spring Boot app
- `docs/` - module-specific docs

## Key Features

- Product catalog with search, filters, and category browsing
- Shopping cart with add/remove/update quantity
- User authentication and protected checkout
- Stripe payment processing for orders
- MySQL-backed persistence for products, users, and orders
- Responsive UI optimized for desktop and mobile
- Containerized deployment with Docker and Kubernetes manifests

## Local Development

### Prerequisites

- Node.js 20+ and npm
- Angular CLI
- Java 17+
- Maven or Gradle
- MySQL 8+
- A Stripe account and API keys

### Environment Setup

Create environment files:

- `backend/.env` for database and Stripe settings
- `frontend/.env` for API base URL and Stripe publishable key

Suggested variables:

- `SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/ecommerce`
- `SPRING_DATASOURCE_USERNAME=your_user`
- `SPRING_DATASOURCE_PASSWORD=your_password`
- `STRIPE_SECRET_KEY=sk_test_...`
- `JWT_SECRET=your_jwt_secret`
- `NG_APP_API_BASE_URL=http://localhost:8080`
- `NG_APP_STRIPE_PUBLISHABLE_KEY=pk_test_...`

### Run Backend

From `backend/`:

```bash
mvn spring-boot:run
```

Or with Gradle:

```bash
./gradlew bootRun
```

### Run Frontend

From `frontend/`:

```bash
npm install
ng serve
```

### Run Tests

```bash
# backend
mvn test

# frontend
npm test
```

## Deployment

- Docker images for frontend and backend
- Kubernetes manifests for deployment, service, and ingress
