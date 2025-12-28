package com.mircea.portofolio.ecommerce.service;

public record StripePaymentIntent(String id, String clientSecret, String status) {}
