package com.mircea.portofolio.ecommerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record CreateOrderRequest(
		@NotEmpty List<@Valid OrderItemRequest> items,
		String currency
) {}
