package com.mircea.portofolio.ecommerce.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {
	private final String secretKey;

	public StripeConfig(@Value("${stripe.secret-key:}") String secretKey) {
		this.secretKey = secretKey;
	}

	@PostConstruct
	public void init() {
		if (secretKey != null && !secretKey.isBlank()) {
			Stripe.apiKey = secretKey;
		}
	}
}
