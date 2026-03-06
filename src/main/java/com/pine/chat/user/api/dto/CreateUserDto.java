package com.pine.chat.user.api.dto;

import lombok.Builder;

@Builder
public record CreateUserDto(
    String username,
    String passwordHash


) {}
