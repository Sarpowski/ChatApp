package com.pine.chat.chatRequest.api;

import com.pine.chat.chatRequest.api.dto.ChatRequestDto;
import com.pine.chat.chatRequest.model.AcceptChatRequestResponse;
import java.util.List;
import java.util.UUID;

public interface ChatRequestService {

  ChatRequestDto sendRequest(UUID senderId, UUID receiverId);

  AcceptChatRequestResponse acceptRequest(UUID requestId, UUID currentUserId);

  ChatRequestDto rejectRequest(UUID requestId, UUID currentUserId);

  List<ChatRequestDto> getPendingRequestsForUser(UUID userId);
}
