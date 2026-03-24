package com.pine.chat.message.model;

import lombok.Cleanup;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.Column;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("chat_messages")
public class ChatMessage {

  @PrimaryKey
  UUID id;

  @Column("message_id")
  UUID messageId;

  @Column("sender_id")
  UUID senderId;

  @Column("content")
  String content;

  @Column("created_at")
  Instant createdAt;

}
