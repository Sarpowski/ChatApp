package com.pine.chat.conversation.controller;

import static com.pine.chat.core.util.helper.ControllerHelper.getCurrentUserId;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.pine.chat.conversation.api.ConversationService;
import com.pine.chat.conversation.api.dto.ConversationDto;
import com.pine.chat.core.util.ApiVersion.ApiVersion;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(ApiVersion.V1 + "/conversations")
@RequiredArgsConstructor
public class ConversationController {

  private final ConversationService conversationService;

  @GetMapping
  public ResponseEntity<List<ConversationDto>> getMyConversations() {
    return ResponseEntity.ok(conversationService.findByUserId(getCurrentUserId()));
  }


  // GET /api/v1/conversations/{id} — get single conversation (must be participant)
  @GetMapping("/{conversationId}")
  public ResponseEntity<ConversationDto> getConversation(@PathVariable UUID conversationId) {
    UUID currentUserId = getCurrentUserId();

    ConversationDto dto = conversationService.findById(conversationId)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "conversation not found"));

    boolean isParticipant = currentUserId.equals(dto.user1Id())
        || currentUserId.equals(dto.user2Id());
    if (!isParticipant) {
      throw new ResponseStatusException(FORBIDDEN, "access denied");
    }

    return ResponseEntity.ok(dto);
  }
}
