package com.portfolio.ecommerce.inventory;

public record OrderItem(
    String sku,
    int quantity
) {
}
