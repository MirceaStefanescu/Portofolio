package com.mircea.portofolio.ecommerce.controller;

import com.mircea.portofolio.ecommerce.dto.CheckoutRequest;
import com.mircea.portofolio.ecommerce.dto.CheckoutResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {
	@PostMapping("/payment-intent")
	public CheckoutResponse createPaymentIntent(@Valid @RequestBody CheckoutRequest request) throws StripeException {
		PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
				.setAmount(request.amount())
				.setCurrency(request.currency().toLowerCase())
				.build();
		PaymentIntent intent = PaymentIntent.create(params);
		return new CheckoutResponse(intent.getClientSecret(), intent.getId());
	}
}
