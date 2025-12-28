package com.example.realtimechat.repository;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.realtimechat.model.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
  Page<ChatMessage> findByRoom_IdOrderByCreatedAtDesc(UUID roomId, Pageable pageable);
}
