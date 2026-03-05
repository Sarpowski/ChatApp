package com.pine.chat.user.api.dto;

import com.pine.chat.core.util.model.RoleEnum;
import lombok.Builder;

@Builder
public record CreateUserDto(
    String username,
    String passwordHash


) {}
