package com.portfolio.ecommerce.order;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderController.class)
class OrderControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private OrderEventPublisher orderEventPublisher;

  @Test
  void createOrderReturnsCreated() throws Exception {
    String payload = """
        {
          "customerId": "cust-123",
          "items": [{ "sku": "SKU-123", "quantity": 2 }]
        }
        """;

    mockMvc.perform(post("/api/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(payload))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.orderId").isNotEmpty())
        .andExpect(jsonPath("$.status").value("CREATED"));

    verify(orderEventPublisher).publish(any(OrderCreatedEvent.class));
  }

  @Test
  void createOrderRejectsInvalidPayload() throws Exception {
    String payload = """
        {
          "items": [{ "sku": "SKU-123", "quantity": 2 }]
        }
        """;

    mockMvc.perform(post("/api/orders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(payload))
        .andExpect(status().isBadRequest());
  }
}
