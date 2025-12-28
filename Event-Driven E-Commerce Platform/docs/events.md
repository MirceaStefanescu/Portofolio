# Event contracts

## Topics
- `order.created`: emitted by the Order service after validating an order request.

## Payloads
### order.created
```json
{
  "orderId": "a4a9b0d2-1c0a-4c4b-9b0e-9f0c2a6f7b9a",
  "customerId": "cust-123",
  "items": [
    { "sku": "SKU-123", "quantity": 2 },
    { "sku": "SKU-456", "quantity": 1 }
  ],
  "createdAt": "2024-05-01T10:00:00Z"
}
```

## Conventions
- Events are JSON-encoded and keyed by `orderId` to keep ordering per order.
- Consumers should treat events as immutable and idempotent.
