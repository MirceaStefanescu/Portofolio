package com.example.realtimechat.model;

import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "chat_rooms")
public class ChatRoom {
  @Id
  @UuidGenerator
  private UUID id;

  @Column(nullable = false, unique = true, length = 120)
  private String name;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  protected ChatRoom() {
  }

  public ChatRoom(String name) {
    this.name = name;
  }

  @PrePersist
  void onCreate() {
    if (createdAt == null) {
      createdAt = Instant.now();
    }
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
