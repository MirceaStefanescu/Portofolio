package com.mircea.portofolio.ecommerce.dto;

import com.mircea.portofolio.ecommerce.model.OrderItem;
import java.math.BigDecimal;

public record OrderItemResponse(
		Long productId,
		String name,
		int quantity,
		BigDecimal unitPrice
) {
	public static OrderItemResponse from(OrderItem item) {
		return new OrderItemResponse(
				item.getProduct().getId(),
				item.getProduct().getName(),
				item.getQuantity(),
				item.getUnitPrice()
		);
	}
}
