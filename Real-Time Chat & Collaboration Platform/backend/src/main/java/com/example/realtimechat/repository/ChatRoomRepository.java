package com.example.realtimechat.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.realtimechat.model.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {
  Optional<ChatRoom> findByName(String name);
}
