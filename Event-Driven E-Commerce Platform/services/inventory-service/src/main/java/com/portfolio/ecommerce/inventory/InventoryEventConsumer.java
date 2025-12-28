package com.portfolio.ecommerce.inventory;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryEventConsumer {
  private static final Logger logger = LoggerFactory.getLogger(InventoryEventConsumer.class);

  private final ObjectMapper objectMapper;
  private final InventoryStore inventoryStore;

  public InventoryEventConsumer(ObjectMapper objectMapper, InventoryStore inventoryStore) {
    this.objectMapper = objectMapper;
    this.inventoryStore = inventoryStore;
  }

  @KafkaListener(topics = "${app.topics.order-created}")
  public void handleOrderCreated(String payload) {
    try {
      OrderCreatedEvent event = objectMapper.readValue(payload, OrderCreatedEvent.class);
      if (event.items() != null) {
        event.items().forEach(item -> inventoryStore.reserve(item.sku(), item.quantity()));
      }
      logger.info("Reserved inventory for orderId={} items={}", event.orderId(),
          event.items() == null ? 0 : event.items().size());
    } catch (Exception ex) {
      logger.error("Failed to process order.created event", ex);
    }
  }
}
