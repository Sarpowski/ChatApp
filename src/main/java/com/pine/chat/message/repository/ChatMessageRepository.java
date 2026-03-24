package com.pine.chat.message.repository;

import com.pine.chat.message.model.ChatMessage;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends CassandraRepository<ChatMessage, UUID> {
  Optional<UUID> findByConversationId();
}
