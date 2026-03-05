package com.pine.chat.core.config;


import com.pine.chat.core.security.JwtAuthenticationFilter;
import com.pine.chat.core.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtService jwtService;


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
      throws Exception {
    http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/**")
            .permitAll()
            .anyRequest()
            .authenticated()
        ).addFilterBefore(
            new JwtAuthenticationFilter(jwtService),
        UsernamePasswordAuthenticationFilter.class
        );
    return http.build();
  }
}
