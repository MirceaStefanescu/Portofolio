package com.example.realtimechat.service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.example.realtimechat.model.ChatEvent;
import com.example.realtimechat.model.ChatEventType;
import com.example.realtimechat.model.ChatMessage;
import com.example.realtimechat.model.ChatMessageResponse;
import com.example.realtimechat.model.ChatRoom;
import com.example.realtimechat.model.ChatRoomRequest;
import com.example.realtimechat.model.ChatRoomResponse;
import com.example.realtimechat.repository.ChatMessageRepository;
import com.example.realtimechat.repository.ChatRoomRepository;

@Service
public class ChatService {
  private final ChatRoomRepository chatRoomRepository;
  private final ChatMessageRepository chatMessageRepository;
  private final KafkaTemplate<String, ChatEvent> kafkaTemplate;
  private final String topic;

  public ChatService(
      ChatRoomRepository chatRoomRepository,
      ChatMessageRepository chatMessageRepository,
      KafkaTemplate<String, ChatEvent> kafkaTemplate,
      @Value("${app.kafka-topic}") String topic
  ) {
    this.chatRoomRepository = chatRoomRepository;
    this.chatMessageRepository = chatMessageRepository;
    this.kafkaTemplate = kafkaTemplate;
    this.topic = topic;
  }

  public List<ChatRoomResponse> listRooms() {
    return chatRoomRepository.findAll().stream()
        .sorted(Comparator.comparing(ChatRoom::getCreatedAt))
        .map(room -> new ChatRoomResponse(room.getId(), room.getName(), room.getCreatedAt()))
        .collect(Collectors.toList());
  }

  public ChatRoomResponse createRoom(ChatRoomRequest request) {
    String name = request.name() == null ? "" : request.name().trim();
    if (!StringUtils.hasText(name)) {
      throw new IllegalArgumentException("Room name is required");
    }

    ChatRoom room = chatRoomRepository.findByName(name)
        .orElseGet(() -> chatRoomRepository.save(new ChatRoom(name)));
    return new ChatRoomResponse(room.getId(), room.getName(), room.getCreatedAt());
  }

  public void handleInboundMessage(UUID roomId, String sender, String content) {
    if (!StringUtils.hasText(content)) {
      throw new IllegalArgumentException("Message content is required");
    }

    String resolvedSender = StringUtils.hasText(sender) ? sender.trim() : "anonymous";
    ChatRoom room = chatRoomRepository.findById(roomId)
        .orElseThrow(() -> new IllegalArgumentException("Room not found"));

    ChatMessage message = chatMessageRepository.save(new ChatMessage(room, resolvedSender, content.trim()));

    ChatEvent event = new ChatEvent(
        message.getId(),
        room.getId(),
        room.getName(),
        message.getSender(),
        message.getContent(),
        message.getCreatedAt(),
        ChatEventType.MESSAGE
    );

    kafkaTemplate.send(topic, room.getId().toString(), event);
  }

  public List<ChatMessageResponse> getRecentMessages(UUID roomId, int limit) {
    if (!chatRoomRepository.existsById(roomId)) {
      throw new IllegalArgumentException("Room not found");
    }

    int size = Math.min(Math.max(limit, 1), 100);
    List<ChatMessage> messages = chatMessageRepository
        .findByRoom_IdOrderByCreatedAtDesc(roomId, PageRequest.of(0, size))
        .getContent();

    return messages.stream()
        .sorted(Comparator.comparing(ChatMessage::getCreatedAt))
        .map(this::toResponse)
        .collect(Collectors.toList());
  }

  private ChatMessageResponse toResponse(ChatMessage message) {
    ChatRoom room = message.getRoom();
    return new ChatMessageResponse(
        message.getId(),
        room.getId(),
        room.getName(),
        message.getSender(),
        message.getContent(),
        message.getCreatedAt()
    );
  }
}
