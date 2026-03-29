package com.pine.chat.conversation.api;


import com.pine.chat.conversation.api.dto.ConversationDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversationService {

  ConversationDto createConversation(UUID userA, UUID userB);

  Optional<ConversationDto> findById(UUID conversationId);

  List<ConversationDto> findByUserId(UUID userId);

  boolean existsBetweenUsers(UUID userA, UUID userB);
}
