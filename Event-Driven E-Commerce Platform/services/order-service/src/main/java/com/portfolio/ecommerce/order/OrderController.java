package com.portfolio.ecommerce.order;

import jakarta.validation.Valid;
import java.time.Instant;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
  private final OrderEventPublisher orderEventPublisher;

  public OrderController(OrderEventPublisher orderEventPublisher) {
    this.orderEventPublisher = orderEventPublisher;
  }

  @PostMapping
  public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
    String orderId = UUID.randomUUID().toString();
    OrderCreatedEvent event = new OrderCreatedEvent(
        orderId,
        request.customerId(),
        request.items(),
        Instant.now());

    orderEventPublisher.publish(event);

    OrderResponse response = new OrderResponse(orderId, "CREATED");
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
