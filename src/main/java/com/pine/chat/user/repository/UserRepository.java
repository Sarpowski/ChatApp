package com.pine.chat.user.repository;

import com.pine.chat.user.model.UserEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

  Optional<UserEntity> findByUsername(String username);

  boolean existsByUsername(String username);

  List<UserEntity> findByIdNot(UUID excludeId);

  @Query("SELECT u FROM UserEntity u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%')) AND u.id != :excludeId")
  List<UserEntity> searchByUsernameContaining(@Param("query") String query, @Param("excludeId") UUID excludeId);
}
