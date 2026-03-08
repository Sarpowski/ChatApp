package com.pine.chat.chatRequest.api;

import com.pine.chat.chatRequest.api.dto.ChatRequestDto;
import java.util.List;
import java.util.UUID;

public interface ChatRequestService {

  ChatRequestDto sendRequest(UUID senderId, UUID receiverId);

  ChatRequestDto acceptRequest(UUID requestId, UUID currentUserId);

  ChatRequestDto rejectRequest(UUID requestId, UUID currentUserId);

  List<ChatRequestDto> getPendingRequestsForUser(UUID userId);
}
