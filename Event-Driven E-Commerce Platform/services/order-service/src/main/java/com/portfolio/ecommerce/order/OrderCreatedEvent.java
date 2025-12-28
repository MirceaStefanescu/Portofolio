package com.portfolio.ecommerce.order;

import java.time.Instant;
import java.util.List;

public record OrderCreatedEvent(
    String orderId,
    String customerId,
    List<OrderItem> items,
    Instant createdAt
) {
}
