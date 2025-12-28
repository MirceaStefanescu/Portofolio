package com.mircea.portofolio.ecommerce.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StripeService {
	private final String secretKey;

	public StripeService(@Value("${stripe.secret-key:}") String secretKey) {
		this.secretKey = secretKey;
		if (secretKey != null && !secretKey.isBlank()) {
			Stripe.apiKey = secretKey;
		}
	}

	public PaymentIntent createPaymentIntent(long amount, String currency, Long orderId) throws StripeException {
		ensureConfigured();
		PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
				.setAmount(amount)
				.setCurrency(currency)
				.setAutomaticPaymentMethods(
						PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
								.setEnabled(true)
								.build()
				)
				.putMetadata("orderId", String.valueOf(orderId))
				.build();
		return PaymentIntent.create(params);
	}

	public PaymentIntent retrievePaymentIntent(String paymentIntentId) throws StripeException {
		ensureConfigured();
		return PaymentIntent.retrieve(paymentIntentId);
	}

	private void ensureConfigured() {
		if (secretKey == null || secretKey.isBlank()) {
			throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, "Stripe secret key is not configured");
		}
	}
}
