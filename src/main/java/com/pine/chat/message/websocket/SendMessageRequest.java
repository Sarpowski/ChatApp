package com.pine.chat.message.websocket;

import java.util.UUID;

public record SendMessageRequest(
    String content,
    UUID messageId
)
{}