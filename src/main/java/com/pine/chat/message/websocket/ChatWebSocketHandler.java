package com.pine.chat.message.websocket;


import static org.springframework.http.HttpStatus.FORBIDDEN;

import com.pine.chat.message.api.MessageService;
import com.pine.chat.message.api.dto.MessageDto;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketHandler {

  private final MessageService messageService;
  private final SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/conversation.{conversationId}.send")
  public void handleMessage(
      @DestinationVariable UUID conversationId,
      @Payload SendMessageRequest request,
      Principal principal
      ) {
    if (principal == null) {
      throw new ResponseStatusException(FORBIDDEN, "not authenticated");
    }

    UUID senderId = UUID.fromString(principal.getName());
    MessageDto saved = messageService.sendMessage(
        conversationId,
        senderId,
        request.content(),
        request.messageId());

    messagingTemplate.convertAndSend(
        "/topic/conversation." + conversationId,
        saved
    );
  }

}
