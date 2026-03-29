package com.pine.chat.user.api;

import com.pine.chat.user.api.dto.CreateUserDto;
import com.pine.chat.user.api.dto.UserDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  Optional<UserDto> findByUsername(String username);

  boolean existsByUsername(String username);

  UserDto createUser(CreateUserDto user);

  Optional<UserDto> verifyCredentials(String username, String password);

  Optional<UserDto> findById(UUID id);


  List<UserDto> findAllExcept(UUID excludeUserId);

  List<UserDto> searchByUsername(String query, UUID excludeUserId);

}
