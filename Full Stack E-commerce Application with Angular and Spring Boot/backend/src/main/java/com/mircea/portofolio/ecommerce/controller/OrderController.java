package com.mircea.portofolio.ecommerce.controller;

import com.mircea.portofolio.ecommerce.dto.ConfirmPaymentRequest;
import com.mircea.portofolio.ecommerce.dto.CreateOrderRequest;
import com.mircea.portofolio.ecommerce.dto.OrderResponse;
import com.mircea.portofolio.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping
	public OrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request, Authentication authentication) {
		return orderService.createOrder(authentication.getName(), request);
	}

	@GetMapping
	public List<OrderResponse> listOrders(Authentication authentication) {
		return orderService.listOrders(authentication.getName());
	}

	@PostMapping("/{orderId}/confirm")
	public OrderResponse confirmPayment(
			@PathVariable Long orderId,
			@Valid @RequestBody ConfirmPaymentRequest request,
			Authentication authentication
	) {
		return orderService.confirmPayment(authentication.getName(), orderId, request.paymentIntentId());
	}
}
