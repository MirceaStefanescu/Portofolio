package com.portfolio.ecommerce.inventory;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private InventoryStore inventoryStore;

  @Test
  void getInventoryReturnsAvailable() throws Exception {
    when(inventoryStore.getAvailable("SKU-123")).thenReturn(12);

    mockMvc.perform(get("/api/inventory/SKU-123"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sku").value("SKU-123"))
        .andExpect(jsonPath("$.available").value(12));
  }
}
