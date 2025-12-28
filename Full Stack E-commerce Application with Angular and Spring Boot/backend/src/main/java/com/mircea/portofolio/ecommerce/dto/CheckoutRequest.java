package com.mircea.portofolio.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CheckoutRequest(
		@Min(1) long amount,
		@NotBlank String currency
) {}
