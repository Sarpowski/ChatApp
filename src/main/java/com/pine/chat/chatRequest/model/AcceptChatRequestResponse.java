package com.pine.chat.chatRequest.model;

import java.time.Instant;
import java.util.UUID;

public record AcceptChatRequestResponse(
    UUID id,
    UUID senderId,
    UUID receiverId,
    ChatRequestStatus status,
    Instant createdAt,
    Instant updatedAt,
    UUID conversationId
) {}