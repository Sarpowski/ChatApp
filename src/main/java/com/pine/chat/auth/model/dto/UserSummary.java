package com.pine.chat.auth.model.dto;

import com.pine.chat.core.util.model.RoleEnum;
import java.util.UUID;

public record UserSummary(
    UUID id,
    String username,
    RoleEnum role
) {
}
