package com.mircea.portofolio.ecommerce.dto;

import com.mircea.portofolio.ecommerce.model.Order;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
		Long id,
		String status,
		BigDecimal totalAmount,
		String currency,
		String paymentIntentId,
		String clientSecret,
		Instant createdAt,
		List<OrderItemResponse> items
) {
	public static OrderResponse from(Order order, String clientSecret) {
		return new OrderResponse(
				order.getId(),
				order.getStatus().name(),
				order.getTotalAmount(),
				order.getCurrency(),
				order.getPaymentIntentId(),
				clientSecret,
				order.getCreatedAt(),
				order.getItems().stream().map(OrderItemResponse::from).toList()
		);
	}
}
