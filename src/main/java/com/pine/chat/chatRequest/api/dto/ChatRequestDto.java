package com.pine.chat.chatRequest.api.dto;

import com.pine.chat.chatRequest.model.ChatRequestStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ChatRequestDto (
    UUID id,
    UUID senderId,
    UUID receiverId,
    ChatRequestStatus status,
    Instant createdAt,
    Instant updateAt
    )
{}
