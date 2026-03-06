package com.pine.chat.conversation.api.dto;


import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ConversationDto (
    UUID id,
    UUID user1Id,
    UUID user2Id,
    Instant createdAt
)
{}
