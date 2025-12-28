package com.example.realtimechat.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import com.example.realtimechat.model.ChatEvent;
import com.example.realtimechat.model.ChatMessageResponse;

@Component
public class ChatEventListener {
  private final SimpMessagingTemplate messagingTemplate;

  public ChatEventListener(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  @KafkaListener(topics = "${app.kafka-topic}")
  public void handleChatEvent(ChatEvent event) {
    ChatMessageResponse payload = new ChatMessageResponse(
        event.messageId(),
        event.roomId(),
        event.roomName(),
        event.sender(),
        event.content(),
        event.createdAt()
    );
    messagingTemplate.convertAndSend("/topic/rooms/" + event.roomId(), payload);
  }
}
