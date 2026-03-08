package com.pine.chat.chatRequest.model;

import java.util.UUID;

public record SendChatRequestDto (
    UUID receiverId
) { }
