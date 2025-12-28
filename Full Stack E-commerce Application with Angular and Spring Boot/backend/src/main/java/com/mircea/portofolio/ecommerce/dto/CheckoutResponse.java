package com.mircea.portofolio.ecommerce.dto;

public record CheckoutResponse(String clientSecret, String paymentIntentId) {}
