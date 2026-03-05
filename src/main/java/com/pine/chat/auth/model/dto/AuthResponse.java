package com.pine.chat.auth.model.dto;

import lombok.Builder;

@Builder
public record AuthResponse(
   String jwtToken,
   String refreshToken,
   UserSummary user

) {}
