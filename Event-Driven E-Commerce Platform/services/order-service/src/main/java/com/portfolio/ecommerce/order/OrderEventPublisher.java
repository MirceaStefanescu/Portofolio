package com.portfolio.ecommerce.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventPublisher {
  private static final Logger logger = LoggerFactory.getLogger(OrderEventPublisher.class);

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;
  private final String orderCreatedTopic;

  public OrderEventPublisher(
      KafkaTemplate<String, String> kafkaTemplate,
      ObjectMapper objectMapper,
      @Value("${app.topics.order-created}") String orderCreatedTopic) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
    this.orderCreatedTopic = orderCreatedTopic;
  }

  public void publish(OrderCreatedEvent event) {
    try {
      String payload = objectMapper.writeValueAsString(event);
      kafkaTemplate.send(orderCreatedTopic, event.orderId(), payload);
      logger.info("Published order.created event orderId={}", event.orderId());
    } catch (JsonProcessingException ex) {
      throw new IllegalStateException("Failed to serialize order event", ex);
    }
  }
}
