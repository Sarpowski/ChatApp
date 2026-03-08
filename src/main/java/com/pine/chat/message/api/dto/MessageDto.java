package com.pine.chat.message.api.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record MessageDto(
    UUID id,
    UUID messageId,
    UUID senderId,
    String content,
    Instant createdAt
    )
{ }
