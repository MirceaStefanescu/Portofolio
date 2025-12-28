package com.example.realtimechat.controller;

import java.security.Principal;
import java.util.UUID;
import jakarta.validation.Valid;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import com.example.realtimechat.model.ChatMessageRequest;
import com.example.realtimechat.service.ChatService;

@Controller
public class ChatWebSocketController {
  private final ChatService chatService;

  public ChatWebSocketController(ChatService chatService) {
    this.chatService = chatService;
  }

  @MessageMapping("/rooms/{roomId}/send")
  public void sendMessage(
      @DestinationVariable UUID roomId,
      @Valid @Payload ChatMessageRequest request,
      Principal principal
  ) {
    String sender = request.sender();
    if ((sender == null || sender.isBlank()) && principal != null) {
      sender = principal.getName();
    }
    chatService.handleInboundMessage(roomId, sender, request.content());
  }
}
