package com.pine.chat.chatRequest.service;


import com.pine.chat.chatRequest.api.ChatRequestService;
import com.pine.chat.chatRequest.api.dto.ChatRequestDto;
import com.pine.chat.chatRequest.model.ChatRequestEntity;
import com.pine.chat.chatRequest.repository.ChatRequestRepository;
import com.pine.chat.conversation.api.ConversationService;
import com.pine.chat.user.api.UserService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRequestServiceImpl implements ChatRequestService {

  private final ChatRequestRepository chatRequestRepository;
  private final UserService userService;
  private final ConversationService conversationService;


  @Override
  public ChatRequestDto sendRequest(UUID senderId, UUID recieverId) {
    return null;
  }


  @Override
  public ChatRequestDto acceptRequest(UUID requestId, UUID currentUserId) {
    return null;
  }

  @Override
  public ChatRequestDto rejectRequest(UUID requestId, UUID currentUserId) {
    return null;
  }

  @Override
  public List<ChatRequestDto> getPendingRequestsForUser(UUID userId) {
    return List.of();
  }


  private static ChatRequestDto toDto(ChatRequestEntity entity) {
    return new ChatRequestDto(
        entity.getId(),
        entity.getSenderId(),
        entity.getReceiverId(),
        entity.getStatus(),
        entity.getCreatedAt(),
        entity.getUpdatedAt()
    );
  }


}
