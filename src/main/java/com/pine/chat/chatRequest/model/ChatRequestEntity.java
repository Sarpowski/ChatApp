package com.pine.chat.chatRequest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "chat_requests",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"sender_id", "receiver_id"})
    }
)@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequestEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  UUID id;

  @Column(name = "sender_id", nullable = false)
  UUID senderId;

  @Column(name = "reciever_id", nullable = false)
  UUID receiverId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  ChatRequestStatus status;

  @Column(nullable = false)
  Instant createdAt;

  Instant updatedAt;
}
