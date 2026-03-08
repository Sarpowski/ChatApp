package com.pine.chat.message.service;

import com.pine.chat.message.api.MessageService;
import com.pine.chat.message.api.dto.MessageDto;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class MessagServiceImpl implements MessageService {

  @Override
  public MessageDto sendMessage(UUID conversationId, UUID senderId, String content) {
    return null;
  }

  @Override
  public List<MessageDto> getMessages(UUID conversationId, int limit) {
    return List.of();
  }
}
