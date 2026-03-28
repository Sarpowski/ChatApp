package com.pine.chat.message.repository;

import com.pine.chat.message.model.ChatMessage;
import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends CassandraRepository<ChatMessage, UUID> {

  @Query("SELECT * FROM chat_messages WHERE conversation_id = ?0 LIMIT ?1")
  Slice<ChatMessage> findByConversationId(UUID conversationId, int limit, Pageable pageable);
}
