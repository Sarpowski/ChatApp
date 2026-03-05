package com.pine.chat.auth.controller;

import com.pine.chat.auth.model.dto.AuthResponse;
import com.pine.chat.auth.model.dto.LoginRequest;
import com.pine.chat.auth.model.dto.RegisterRequest;
import com.pine.chat.auth.service.AuthService;
import com.pine.chat.core.util.ApiVersion.ApiVersion;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiVersion.V1 + "/auth")
@RequiredArgsConstructor
public class AuthController {

  private static final String REFRESH_COOKIE_NAME = "refreshToken";

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(
      @Valid @RequestBody RegisterRequest request,
      HttpServletResponse response
  ) {
    AuthResponse auth = authService.register(request);
    addRefreshCookie(response, auth.refreshToken(), authService.getRefreshCookieMaxAgeSeconds());
    return ResponseEntity.ok(auth);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(
      @Valid @RequestBody LoginRequest request,
      HttpServletResponse response
  ) {
    AuthResponse auth = authService.login(request);
    addRefreshCookie(response, auth.refreshToken(), authService.getRefreshCookieMaxAgeSeconds());
    return ResponseEntity.ok(auth);
  }

  @PostMapping("/refresh")
  public ResponseEntity<AuthResponse> refresh(
      @CookieValue(name = REFRESH_COOKIE_NAME, required = false) String refreshToken,
      HttpServletResponse response
  ) {
    AuthResponse auth = authService.refresh(refreshToken);
    addRefreshCookie(response, auth.refreshToken(), authService.getRefreshCookieMaxAgeSeconds());
    return ResponseEntity.ok(auth);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(
      @CookieValue(name = REFRESH_COOKIE_NAME, required = false) String refreshToken,
      HttpServletResponse response
  ) {
    authService.logout(refreshToken);
    clearRefreshCookie(response);
    return ResponseEntity.noContent().build();
  }

  private static void addRefreshCookie(HttpServletResponse response, String refreshToken, long maxAgeSeconds) {
    if (refreshToken == null || refreshToken.isBlank()) {
      return;
    }

    ResponseCookie cookie = ResponseCookie.from(REFRESH_COOKIE_NAME, refreshToken)
        .httpOnly(true)
        .secure(false)
        .sameSite("Strict")
        .path("/")
        .maxAge(maxAgeSeconds)
        .build();

    response.addHeader("Set-Cookie", cookie.toString());
  }

  private static void clearRefreshCookie(HttpServletResponse response) {
    ResponseCookie cookie = ResponseCookie.from(REFRESH_COOKIE_NAME, "")
        .httpOnly(true)
        .secure(false)
        .sameSite("Strict")
        .path("/")
        .maxAge(0)
        .build();

    response.addHeader("Set-Cookie", cookie.toString());
  }
}
