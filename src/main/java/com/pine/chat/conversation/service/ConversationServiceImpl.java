package com.pine.chat.conversation.service;

import com.pine.chat.conversation.api.ConversationService;
import com.pine.chat.conversation.api.dto.ConversationDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
  @Override
  public ConversationDto createConverstaion(UUID userA, UUID userB) {
    return null;
  }

  @Override
  public Optional<ConversationDto> findById(UUID conversationId) {
    return Optional.empty();
  }

  @Override
  public List<ConversationDto> findByUserId(UUID userId) {
    return List.of();
  }

  @Override
  public boolean existsBetweenUsers(UUID userA, UUID userB) {
    return false;
  }
}
