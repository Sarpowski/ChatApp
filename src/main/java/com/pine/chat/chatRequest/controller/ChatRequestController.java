package com.pine.chat.chatRequest.controller;


import static com.pine.chat.core.util.helper.controllerHelper.getCurrnetUserId;

import com.pine.chat.chatRequest.api.ChatRequestService;
import com.pine.chat.chatRequest.api.dto.ChatRequestDto;
import com.pine.chat.chatRequest.model.SendChatRequestDto;
import com.pine.chat.core.util.ApiVersion.ApiVersion;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiVersion.V1 + "/chat-requests")
@RequiredArgsConstructor
public class ChatRequestController {

  private final ChatRequestService chatRequestService;

  @PostMapping
  ResponseEntity<ChatRequestDto> sendRequest(@RequestBody ChatRequestDto requestDto) {

    return ResponseEntity.ok(chatRequestService
        .sendRequest(getCurrnetUserId(),
            requestDto.receiverId()));
  }

  @PostMapping("/{requestId}/accept")
  void acceptRequest(@RequestParam UUID requestId) {
    chatRequestService.acceptRequest(requestId, getCurrnetUserId());
  }

  @PostMapping("/{requestId}/reject")
  void rejectRequest(@RequestParam UUID requestId) {
    chatRequestService.rejectRequest(requestId, getCurrnetUserId());
  }

  @GetMapping("/pending")
  ResponseEntity<List> getPending() {
    return ResponseEntity.ok(chatRequestService
        .getPendingRequestsForUser(getCurrnetUserId()));
  }


}
