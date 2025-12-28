package com.portfolio.ecommerce.inventory;

public record InventoryResponse(
    String sku,
    int available
) {
}
