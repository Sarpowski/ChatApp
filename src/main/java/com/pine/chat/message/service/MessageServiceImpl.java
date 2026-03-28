package com.pine.chat.message.service;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.pine.chat.conversation.api.ConversationService;
import com.pine.chat.message.api.MessageService;
import com.pine.chat.message.api.dto.MessageDto;
import com.pine.chat.message.model.ChatMessage;
import com.pine.chat.message.model.ChatMessageKey;
import com.pine.chat.message.repository.ChatMessageRepository;
import org.springframework.data.domain.Pageable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

  private final ChatMessageRepository chatMessageRepository;
  private final ConversationService conversationService;

  @Override
  public MessageDto sendMessage(UUID conversationId, UUID senderId, String content) {
    var conversation = conversationService.findById(conversationId)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "conversation not found"));
    boolean isParticipant = senderId.equals(conversation.user1Id())
        || senderId.equals(conversation.user2Id());

    if (!isParticipant) {
      throw new ResponseStatusException(FORBIDDEN, "user is not a participant of this conversation");
    }
    var key = new ChatMessageKey(conversationId, Instant.now(), UUID.randomUUID());

    var message = ChatMessage.builder()
        .key(key)
        .senderId(senderId)
        .content(content)
        .build();

    chatMessageRepository.save(message);

    return toDto(message);
  }



  @Override
  public List<MessageDto> getMessages(UUID conversationId, int limit) {
    conversationService.findById(conversationId)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "conversation not found"));

    return chatMessageRepository
        .findByConversationId(conversationId, limit, PageRequest.of(0, limit))
        .stream()
        .map(MessageServiceImpl::toDto)
        .toList();

  }

  private static MessageDto toDto(ChatMessage message) {
    return MessageDto.builder()
        .conversationId(message.getKey().getConversationId())
        .messageId(message.getKey().getMessageId())
        .senderId(message.getSenderId())
        .content(message.getContent())
        .createdAt(message.getKey().getCreatedAt())
        .build();
  }
}
