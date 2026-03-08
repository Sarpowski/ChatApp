package com.pine.chat.message.api;


import com.pine.chat.message.api.dto.MessageDto;
import java.util.List;
import java.util.UUID;

public interface MessageService {

  MessageDto sendMessage(UUID conversationId, UUID senderId, String content);

  List<MessageDto> getMessages(UUID conversationId, int limit);
}
