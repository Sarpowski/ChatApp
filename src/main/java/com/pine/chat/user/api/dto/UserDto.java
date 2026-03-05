package com.pine.chat.user.api.dto;

import com.pine.chat.core.util.model.RoleEnum;
import java.util.UUID;

public record UserDto(
    UUID userId,
    String username,
    RoleEnum role
) {
}
