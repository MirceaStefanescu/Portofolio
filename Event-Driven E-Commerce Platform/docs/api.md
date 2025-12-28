# API

## Order service
- `POST /api/orders`
  - Body: `{ "customerId": "cust-123", "items": [{ "sku": "SKU-123", "quantity": 2 }] }`
  - Response: `{ "orderId": "...", "status": "CREATED" }`

## Inventory service
- `GET /api/inventory/{sku}`
  - Response: `{ "sku": "SKU-123", "available": 42 }`
