package com.pine.chat.chatRequest.service;


import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.pine.chat.chatRequest.api.ChatRequestService;
import com.pine.chat.chatRequest.api.dto.ChatRequestDto;
import com.pine.chat.chatRequest.model.ChatRequestEntity;
import com.pine.chat.chatRequest.model.ChatRequestStatus;
import com.pine.chat.chatRequest.repository.ChatRequestRepository;
import com.pine.chat.conversation.api.ConversationService;
import com.pine.chat.user.api.UserService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import jnr.ffi.annotations.In;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ChatRequestServiceImpl implements ChatRequestService {

  private final ChatRequestRepository chatRequestRepository;
  private final UserService userService;
  private final ConversationService conversationService;


  @Override
  public ChatRequestDto sendRequest(UUID senderId, UUID receiverId) {
    if (senderId == receiverId) {
      throw new ResponseStatusException(BAD_REQUEST, "senderId and receiverId is equals");
    }

    userService.findById(senderId)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "sender not found"));
    userService.findById(receiverId)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "receiver not found"));

    if (conversationService.existsBetweenUsers(senderId, receiverId)) {
      throw new ResponseStatusException(CONFLICT, "conversation already exists");
    }
    if (chatRequestRepository.existsBySenderIdAndReceiverId(senderId, receiverId)) {
      throw new ResponseStatusException(CONFLICT, "request already sent");
    }
    var entity = ChatRequestEntity.builder()
        .senderId(senderId)
        .receiverId(receiverId)
        .status(ChatRequestStatus.PENDING)
        .createdAt(Instant.now())
        .build();
    var savedEntity = chatRequestRepository.save(entity);

    return toDto(savedEntity);
  }


  @Override
  public ChatRequestDto acceptRequest(UUID requestId, UUID currentUserId) {
    var entity = chatRequestRepository.findById(requestId)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "reqeust not found"));

    if (!entity.getReceiverId().equals(currentUserId)) {
      throw new ResponseStatusException(FORBIDDEN, "only the receiver can accept");
    }

    if (entity.getStatus() != ChatRequestStatus.PENDING) {
      throw new ResponseStatusException(CONFLICT, "request is no longer pending");
    }

    entity.setStatus(ChatRequestStatus.ACCEPTED);
    entity.setUpdatedAt(Instant.now());
    chatRequestRepository.save(entity);
    conversationService.createConverstaion(entity.getSenderId(), entity.getReceiverId());
    return toDto(entity);
  }

  @Override
  public ChatRequestDto rejectRequest(UUID requestId, UUID currentUserId) {
    var entity = chatRequestRepository.findById(requestId)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "request not found"));
    if (!entity.getReceiverId().equals(currentUserId)) {
      throw new ResponseStatusException(FORBIDDEN, "only the receiver can accept");
    }

    if (entity.getStatus() != ChatRequestStatus.PENDING) {
      throw new ResponseStatusException(CONFLICT, "request is no longer pending");
    }
    entity.setStatus(ChatRequestStatus.REJECTED);
    entity.setUpdatedAt(Instant.now());
    chatRequestRepository.save(entity);

    return toDto(entity);
  }

  @Override
  public List<ChatRequestDto> getPendingRequestsForUser(UUID userId) {
    return chatRequestRepository.findByReceiverIdAndStatus(userId, ChatRequestStatus.PENDING)
        .stream()
        .map(ChatRequestServiceImpl::toDto)
        .toList();

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
