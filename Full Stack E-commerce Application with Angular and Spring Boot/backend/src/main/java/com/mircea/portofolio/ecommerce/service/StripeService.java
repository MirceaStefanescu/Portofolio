package com.mircea.portofolio.ecommerce.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {
	private final String secretKey;
	private final boolean configured;

	public StripeService(@Value("${stripe.secret-key:}") String secretKey) {
		this.secretKey = secretKey;
		this.configured = secretKey != null && !secretKey.isBlank();
		if (configured) {
			Stripe.apiKey = secretKey;
		}
	}

	public StripePaymentIntent createPaymentIntent(long amount, String currency, Long orderId) throws StripeException {
		if (!configured) {
			return mockIntent("requires_payment_method");
		}
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
		PaymentIntent intent = PaymentIntent.create(params);
		return new StripePaymentIntent(intent.getId(), intent.getClientSecret(), intent.getStatus());
	}

	public StripePaymentIntent retrievePaymentIntent(String paymentIntentId) throws StripeException {
		if (!configured) {
			return new StripePaymentIntent(paymentIntentId, null, "succeeded");
		}
		PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);
		return new StripePaymentIntent(intent.getId(), intent.getClientSecret(), intent.getStatus());
	}

	private StripePaymentIntent mockIntent(String status) {
		String id = "mock_pi_" + UUID.randomUUID().toString().replace("-", "");
		String secret = "mock_secret_" + UUID.randomUUID().toString().replace("-", "");
		return new StripePaymentIntent(id, secret, status);
	}
}
