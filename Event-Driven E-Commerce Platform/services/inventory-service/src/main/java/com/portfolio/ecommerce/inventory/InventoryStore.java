package com.portfolio.ecommerce.inventory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class InventoryStore {
  private final Map<String, Integer> stock = new ConcurrentHashMap<>();

  public InventoryStore() {
    stock.put("SKU-123", 120);
    stock.put("SKU-456", 80);
    stock.put("SKU-789", 45);
  }

  public int getAvailable(String sku) {
    return stock.getOrDefault(sku, 0);
  }

  public void reserve(String sku, int quantity) {
    stock.compute(sku, (key, current) -> {
      int available = current == null ? 0 : current;
      int updated = available - quantity;
      return Math.max(updated, 0);
    });
  }
}
