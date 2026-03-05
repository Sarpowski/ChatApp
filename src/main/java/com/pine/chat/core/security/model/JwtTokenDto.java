package com.pine.chat.core.security.model;

public record JwtTokenDto(
    String jwtToken,
    String refreshToken
) {
}
