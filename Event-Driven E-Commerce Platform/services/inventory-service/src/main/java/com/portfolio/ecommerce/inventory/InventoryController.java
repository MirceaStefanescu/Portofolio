package com.portfolio.ecommerce.inventory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
  private final InventoryStore inventoryStore;

  public InventoryController(InventoryStore inventoryStore) {
    this.inventoryStore = inventoryStore;
  }

  @GetMapping("/{sku}")
  public InventoryResponse getInventory(@PathVariable String sku) {
    int available = inventoryStore.getAvailable(sku);
    return new InventoryResponse(sku, available);
  }
}
