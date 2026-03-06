package com.pine.chat.user.service.impl;

import com.pine.chat.core.util.model.RoleEnum;
import com.pine.chat.user.api.UserService;
import com.pine.chat.user.model.UserEntity;
import com.pine.chat.user.api.dto.CreateUserDto;
import com.pine.chat.user.api.dto.UserDto;
import com.pine.chat.user.repository.UserRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;


  @Override
  public Optional<UserDto> findByUsername(String username) {
    return userRepository.findByUsername(username)
        .map(user -> new UserDto(
            user.getId(),
            user.getUsername(),
            user.getRole()
        ));
  }

  @Override
  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  @Override
  public UserDto createUser(CreateUserDto user) {
    var userEntity = UserEntity.builder()
        .username(user.username())
        .passwordHash(user.passwordHash())
        .createdAt(Instant.now())
        .role(RoleEnum.USER)
        .build();
    var savedEntity = userRepository.save(userEntity);

    return new UserDto(
        savedEntity.getId(),
        savedEntity.getUsername(),
        savedEntity.getRole()
    );
  }

  @Override
  public Optional<UserDto> verifyCredentials(String username, String password) {
    return userRepository.findByUsername(username)
        .filter(user -> passwordEncoder.matches(password, user.getPasswordHash()))
        .map(user -> new UserDto(user.getId(),user.getUsername(), user.getRole()));

  }

  @Override
  public Optional<UserDto> findById(UUID id) {
    return userRepository.findById(id)
        .map(user -> new UserDto(
            user.getId(),
            user.getUsername(),
            user.getRole())
        );
  }


}
