package com.example.realtimechat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.example.realtimechat.model.ChatEvent;
import com.example.realtimechat.model.ChatMessage;
import com.example.realtimechat.model.ChatRoom;
import com.example.realtimechat.model.ChatRoomRequest;
import com.example.realtimechat.model.ChatRoomResponse;
import com.example.realtimechat.repository.ChatMessageRepository;
import com.example.realtimechat.repository.ChatRoomRepository;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {
  private static final String TOPIC = "chat-messages";

  @Mock
  private ChatRoomRepository chatRoomRepository;

  @Mock
  private ChatMessageRepository chatMessageRepository;

  @Mock
  private KafkaTemplate<String, ChatEvent> kafkaTemplate;

  private ChatService chatService;

  @BeforeEach
  void setUp() {
    chatService = new ChatService(chatRoomRepository, chatMessageRepository, kafkaTemplate, TOPIC);
  }

  @Test
  void createRoomRejectsBlankName() {
    assertThrows(IllegalArgumentException.class,
        () -> chatService.createRoom(new ChatRoomRequest("   ")));
  }

  @Test
  void createRoomPersistsAndReturnsResponse() throws Exception {
    ChatRoom room = new ChatRoom("General");
    UUID roomId = UUID.randomUUID();
    Instant createdAt = Instant.parse("2024-05-01T10:00:00Z");
    setField(room, "id", roomId);
    setField(room, "createdAt", createdAt);

    when(chatRoomRepository.findByName("General")).thenReturn(Optional.empty());
    when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(room);

    ChatRoomResponse response = chatService.createRoom(new ChatRoomRequest("General"));

    assertEquals(roomId, response.id());
    assertEquals("General", response.name());
    assertEquals(createdAt, response.createdAt());
  }

  @Test
  void handleInboundMessageRejectsEmptyContent() {
    UUID roomId = UUID.randomUUID();
    assertThrows(IllegalArgumentException.class,
        () -> chatService.handleInboundMessage(roomId, "alex", "   "));
    verifyNoInteractions(chatRoomRepository, chatMessageRepository, kafkaTemplate);
  }

  @Test
  void handleInboundMessagePublishesEvent() throws Exception {
    UUID roomId = UUID.randomUUID();
    ChatRoom room = new ChatRoom("General");
    setField(room, "id", roomId);
    setField(room, "createdAt", Instant.now());

    ChatMessage saved = new ChatMessage(room, "alex", "hello");
    setField(saved, "id", UUID.randomUUID());
    setField(saved, "createdAt", Instant.now());

    when(chatRoomRepository.findById(roomId)).thenReturn(Optional.of(room));
    when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(saved);

    chatService.handleInboundMessage(roomId, "alex", "hello");

    verify(kafkaTemplate).send(eq(TOPIC), eq(roomId.toString()), any(ChatEvent.class));
  }

  @Test
  void getRecentMessagesRejectsUnknownRoom() {
    UUID roomId = UUID.randomUUID();
    when(chatRoomRepository.existsById(roomId)).thenReturn(false);

    assertThrows(IllegalArgumentException.class, () -> chatService.getRecentMessages(roomId, 20));
  }

  private static void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }
}
