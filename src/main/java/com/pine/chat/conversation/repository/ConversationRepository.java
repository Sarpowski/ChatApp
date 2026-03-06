package com.pine.chat.conversation.repository;

import com.pine.chat.conversation.model.ConversationEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<ConversationEntity, UUID> {

  List<ConversationEntity> findByUser1IdOrUser2Id(UUID user1Id, UUID user2Id);

  boolean existsByUser1IdAndUser2Id(UUID user1Id, UUID user2Id);
}
