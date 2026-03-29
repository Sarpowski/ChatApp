package com.pine.chat.conversation.service;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.pine.chat.conversation.api.ConversationService;
import com.pine.chat.conversation.api.dto.ConversationDto;
import com.pine.chat.conversation.model.ConversationEntity;
import com.pine.chat.conversation.repository.ConversationRepository;
import com.pine.chat.user.api.UserService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

 private final ConversationRepository conversationRepository;
  private final UserService userService;

  @Override
  public ConversationDto createConversation(UUID userA, UUID userB) {

    var chatId = normalize(userA, userB);
    userService.findById(userA)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "user not found"));
    userService.findById(userB)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "user not found"));

    if(existsBetweenUsers(chatId[0], chatId[1])) {
      throw new ResponseStatusException(CONFLICT, "conversation already exists");
    }

    var entity = ConversationEntity.builder()
        .user1Id(chatId[0])
        .user2Id(chatId[1])
        .createdAt(Instant.now())
        .build();
    var savedConvo = conversationRepository.save(entity);
    return toDto(savedConvo);
  }

  @Override
  public Optional<ConversationDto> findById(UUID conversationId) {
    return conversationRepository.findById(conversationId)
        .map(convo -> new ConversationDto(
            convo.getId(),
            convo.getUser1Id(),
            convo.getUser2Id(),
            convo.getCreatedAt())
        );
  }

  @Override
  public List<ConversationDto> findByUserId(UUID userId) {
    return conversationRepository.findByUser1IdOrUser2Id(userId, userId)
        .stream()
        .map(ConversationServiceImpl::toDto).toList();
  }

  @Override
  public boolean existsBetweenUsers(UUID userA, UUID userB) {
    UUID[] ids = normalize(userA, userB); // ✅ add this
    return conversationRepository.existsByUser1IdAndUser2Id(ids[0], ids[1]);
  }

  private static UUID[] normalize(UUID a, UUID b) {
    return a.compareTo(b) < 0 ? new UUID[]{a, b} : new UUID[]{b, a};
  }
  private static ConversationDto toDto(ConversationEntity entity) {
    return new ConversationDto(
        entity.getId(),
        entity.getUser1Id(),
        entity.getUser2Id(), entity.getCreatedAt());
  }


}
