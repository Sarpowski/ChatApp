package com.pine.chat.core.security;

import com.pine.chat.core.security.model.JwtTokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final SecretKey key;

  private final long expirationMs;
  private final long refreshExpirationMs;

  public JwtService(
      @Value("${spring.security.jwt.secret}") String secret,
      @Value("${spring.security.jwt.expiration}") long expirationMs,
      @Value("${spring.security.jwt.refresh-expiration:0}") long refreshExpirationMs
  ) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    this.expirationMs = expirationMs;
    this.refreshExpirationMs = refreshExpirationMs;
  }

  public String generateToken(String userId, Map<String, Object> claims) {
    return generateToken(userId, claims, false);
  }

  public String generateToken(String userId, Map<String, Object> claims, boolean isRefreshToken) {

    long ttlMs = isRefreshToken ? refreshExpirationMs : expirationMs;

    var builder = Jwts.builder()
        .setSubject(userId)
        .addClaims(claims)
        .setIssuedAt(new Date())
        .signWith(key, SignatureAlgorithm.HS256);

    if (!(isRefreshToken && ttlMs <= 0)) {
      builder.setExpiration(new Date(System.currentTimeMillis() + ttlMs));
    }

    return builder.compact();
  }

  public JwtTokenDto generateJwtPair(String userId, Map<String, Object> claims) {

    String accessToken = generateToken(userId, claims, false);
    String refreshToken = generateToken(userId, claims, true);

    return new JwtTokenDto(accessToken, refreshToken);
  }

  public Claims parseToken(String token) {

    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public String extractUserId(String token) {
    return parseToken(token).getSubject();
  }

  public String extractRole(String token) {
    return parseToken(token).get("role", String.class);
  }
}
