package com.pine.chat.message.controller;


import static com.pine.chat.core.util.helper.ControllerHelper.getCurrentUserId;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.pine.chat.conversation.api.ConversationService;
import com.pine.chat.core.util.ApiVersion.ApiVersion;
import com.pine.chat.message.api.MessageService;
import com.pine.chat.message.api.dto.MessageDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(ApiVersion.V1 + "/conversations/{conversationId}/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;
  private final ConversationService conversationService;

  @GetMapping
  public ResponseEntity<List<MessageDto>> getMessages(
      @PathVariable UUID conversationId,
      @RequestParam(defaultValue = "50") int limit
  ) {
    UUID currentUserId = getCurrentUserId();

    var conversation = conversationService.findById(conversationId)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "conversation not found"));

    boolean isParticipant = currentUserId.equals(conversation.user1Id())
        || currentUserId.equals(conversation.user2Id());
    if (!isParticipant) {
      throw new ResponseStatusException(FORBIDDEN, "access denied");
    }

    return ResponseEntity.ok(messageService.getMessages(conversationId, limit));
  }

}
