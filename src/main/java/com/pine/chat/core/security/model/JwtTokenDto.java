package com.pine.chat.core.security.model;

public record JwtTokenDto(
    String accessToken,
    String refreshToken
) {
}
