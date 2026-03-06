package com.pine.chat.auth.service;

import com.pine.chat.auth.model.dto.AuthResponse;
import com.pine.chat.auth.model.dto.LoginRequest;
import com.pine.chat.auth.model.dto.RegisterRequest;
import com.pine.chat.auth.model.dto.UserSummary;
import com.pine.chat.auth.model.entity.RefreshTokenEntity;
import com.pine.chat.auth.repository.RefreshTokenRepository;
import com.pine.chat.core.security.JwtService;
import com.pine.chat.core.security.model.JwtTokenDto;
import com.pine.chat.user.api.UserService;
import com.pine.chat.user.api.dto.CreateUserDto;
import com.pine.chat.user.api.dto.UserDto;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserService userService;

  private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  private final HexFormat hexFormat = HexFormat.of();

  @Value("${spring.security.jwt.refresh-expiration:0}")
  private long refreshExpirationMs;

  public long getRefreshCookieMaxAgeSeconds() {
    if (refreshExpirationMs <= 0) {
      return -1;
    }
    return Math.max(1, refreshExpirationMs / 1000);
  }

  @Transactional
  public AuthResponse register(RegisterRequest request) {
    String username = normalizeUsername(request.username());

    if (userService.existsByUsername(username)) {
      throw new ResponseStatusException(CONFLICT, "username already exists");
    }

    String passwordHash = passwordEncoder.encode(request.password());

    UserDto user = userService.createUser(
        new CreateUserDto(username, passwordHash)
    );

    return issueTokens(user);
  }

  @Transactional
  public AuthResponse login(LoginRequest request) {
    String username = normalizeUsername(request.username());

    UserDto user = userService.verifyCredentials(username, request.password())
        .orElseThrow(()-> new ResponseStatusException(UNAUTHORIZED, "invalid credentials"));
    return issueTokens(user);
  }

  @Transactional
  public AuthResponse refresh(String refreshToken) {
    if (refreshToken == null || refreshToken.isBlank()) {
      throw new ResponseStatusException(UNAUTHORIZED, "missing refresh token");
    }

    String tokenHash = sha256Hex(refreshToken);
    Instant now = Instant.now();

    RefreshTokenEntity existing = refreshTokenRepository.findByTokenHash(tokenHash)
        .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "invalid refresh token"));

    if (existing.isRevoked() || existing.isExpired(now)) {
      throw new ResponseStatusException(UNAUTHORIZED, "refresh token expired or revoked");
    }

    existing.setRevokedAt(now);
    refreshTokenRepository.save(existing);

    var userId = existing.getUserId();
    UserDto dto = userService.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "user not found"));
    return issueTokens(dto);
  }

  @Transactional
  public void logout(String refreshToken) {
    if (refreshToken == null || refreshToken.isBlank()) {
      return;
    }

    String tokenHash = sha256Hex(refreshToken);
    refreshTokenRepository.findByTokenHash(tokenHash).ifPresent(token -> {
      if (!token.isRevoked()) {
        token.setRevokedAt(Instant.now());
        refreshTokenRepository.save(token);
      }
    });
  }

  private AuthResponse issueTokens(UserDto user) {
    Map<String, Object> claims = Map.of("role", user.role().name());

    JwtTokenDto pair = jwtService.generateJwtPair(user.userId().toString(), claims);

    persistRefreshToken(user.userId(), pair.refreshToken());

    return AuthResponse.builder()
        .jwtToken(pair.accessToken())
        .refreshToken(pair.refreshToken())
        .user(new UserSummary(user.userId(), user.username(), user.role()))
        .build();
  }

  private void persistRefreshToken(UUID userId, String refreshToken) {
    if (refreshToken == null || refreshToken.isBlank()) {
      throw new ResponseStatusException(BAD_REQUEST, "refresh token generation disabled" );
    }

    Instant now = Instant.now();

    RefreshTokenEntity entity = new RefreshTokenEntity();
    entity.setUserId(userId);
    entity.setTokenHash(sha256Hex(refreshToken));
    entity.setIssuedAt(now);

    if (refreshExpirationMs > 0) {
      entity.setExpiresAt(now.plusMillis(refreshExpirationMs));
    }

    refreshTokenRepository.save(entity);
  }

  private String sha256Hex(String value) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
      return hexFormat.formatHex(hash);
    } catch (Exception e) {
      throw new IllegalStateException("sha-256 unavailable", e);
    }
  }

  private static String normalizeUsername(String username) {
    if (username == null) {
      throw new ResponseStatusException(BAD_REQUEST, "username is required");
    }
    String normalized = username.trim();
    if (normalized.isEmpty()) {
      throw new ResponseStatusException(BAD_REQUEST, "username is required");
    }
    return normalized;
  }
}

