package com.pine.chat.core.config;

import com.pine.chat.core.security.JwtService;
import io.jsonwebtoken.Claims;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {
  private final JwtService jwtService;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor =
        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

    if (accessor == null || accessor.getCommand() != StompCommand.CONNECT) {
      return message; // only authenticate on CONNECT
    }

    String authHeader = accessor.getFirstNativeHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new IllegalArgumentException("missing or invalid Authorization header");
    }

    String token = authHeader.substring(7);

    try {
      Claims claims = jwtService.parseToken(token);
      String userId = claims.getSubject();
      String role = claims.get("role", String.class);

      var authorities = (role == null || role.isBlank())
          ? List.<SimpleGrantedAuthority>of()
          : List.of(new SimpleGrantedAuthority("ROLE_" + role));

      var auth = new UsernamePasswordAuthenticationToken(userId, null, authorities);
      accessor.setUser(auth);
    } catch (Exception e) {
      throw new IllegalArgumentException("invalid JWT token", e);
    }

    return message;
  }


}
