package com.portfolio.ecommerce.order;

public record OrderResponse(
    String orderId,
    String status
) {
}
