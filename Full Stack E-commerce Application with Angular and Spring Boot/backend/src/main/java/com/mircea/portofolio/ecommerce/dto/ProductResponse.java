package com.mircea.portofolio.ecommerce.dto;

import com.mircea.portofolio.ecommerce.model.Product;
import java.math.BigDecimal;

public record ProductResponse(
		Long id,
		String name,
		String description,
		BigDecimal price,
		String imageUrl,
		String category
) {
	public static ProductResponse from(Product product) {
		return new ProductResponse(
				product.getId(),
				product.getName(),
				product.getDescription(),
				product.getPrice(),
				product.getImageUrl(),
				product.getCategory()
		);
	}
}
