package com.mircea.portofolio.ecommerce.controller;

import com.mircea.portofolio.ecommerce.dto.ProductResponse;
import com.mircea.portofolio.ecommerce.service.ProductService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public List<ProductResponse> listProducts() {
		return productService.listProducts();
	}

	@GetMapping("/{id}")
	public ProductResponse getProduct(@PathVariable Long id) {
		return productService.getProduct(id);
	}
}
