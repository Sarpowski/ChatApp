package com.pine.chat.chatRequest.controller;


import static com.pine.chat.core.util.helper.ControllerHelper.getCurrentUserId;

import com.pine.chat.chatRequest.api.ChatRequestService;
import com.pine.chat.chatRequest.api.dto.ChatRequestDto;
import com.pine.chat.chatRequest.model.AcceptChatRequestResponse;
import com.pine.chat.core.util.ApiVersion.ApiVersion;
import com.pine.chat.user.api.UserService;
import com.pine.chat.user.api.dto.UserDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  private final UserService userService;

  @PostMapping
  ResponseEntity<ChatRequestDto> sendRequest(@RequestBody ChatRequestDto requestDto) {

    return ResponseEntity.ok(chatRequestService
        .sendRequest(getCurrentUserId(),
            requestDto.receiverId()));
  }

  @PostMapping("/{requestId}/accept")
  ResponseEntity<AcceptChatRequestResponse> acceptRequest(@PathVariable UUID requestId) {
    return ResponseEntity.ok(chatRequestService.acceptRequest(requestId, getCurrentUserId()));
  }

  @PostMapping("/{requestId}/reject")
  ResponseEntity<ChatRequestDto> rejectRequest(@PathVariable UUID requestId) {
    return ResponseEntity.ok(chatRequestService.rejectRequest(requestId, getCurrentUserId()));
  }

  @GetMapping("/pending")
  ResponseEntity<List<ChatRequestDto>> getPending() {
    return ResponseEntity.ok(chatRequestService
        .getPendingRequestsForUser(getCurrentUserId()));
  }


  @GetMapping
  public ResponseEntity<List<UserDto>> getUsers(
      @RequestParam(required = false) String query
  ) {
    var currentUserId = getCurrentUserId();
    var users = userService.searchByUsername(query, currentUserId);
    return ResponseEntity.ok(users);
  }
}
