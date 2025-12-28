package com.mircea.portofolio.ecommerce.service;

import com.mircea.portofolio.ecommerce.dto.ProductResponse;
import com.mircea.portofolio.ecommerce.model.Product;
import com.mircea.portofolio.ecommerce.repository.ProductRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductService {
	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public List<ProductResponse> listProducts() {
		return productRepository.findAll().stream()
				.map(ProductResponse::from)
				.toList();
	}

	public ProductResponse getProduct(Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
		return ProductResponse.from(product);
	}
}
