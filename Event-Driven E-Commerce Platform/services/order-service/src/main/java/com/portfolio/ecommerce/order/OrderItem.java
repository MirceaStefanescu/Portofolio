package com.portfolio.ecommerce.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record OrderItem(
    @NotBlank String sku,
    @Positive int quantity
) {
}
