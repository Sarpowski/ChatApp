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
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

  private UserRepository userRepository;

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

    if (userRepository.existsByUsername(user.username())) {
      throw new RuntimeException("User already Exists");
    }

    var userEntity = UserEntity.builder()
        .username(user.username())
        .passwordHash(user.passwordHash())
        .createdAt(Instant.now())
        .role(RoleEnum.USER)
        .build();
    userRepository.save(userEntity);

    return new UserDto(
        UUID.randomUUID(),
        user.username(),
        RoleEnum.USER
    );
  }

  @Override
  public Object findWithPasswordByUsername(String username) {
    return null;
  }


}
