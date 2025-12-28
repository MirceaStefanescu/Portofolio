package com.mircea.portofolio.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;

public record ConfirmPaymentRequest(@NotBlank String paymentIntentId) {}
