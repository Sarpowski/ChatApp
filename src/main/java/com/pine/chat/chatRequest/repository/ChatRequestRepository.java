package com.pine.chat.chatRequest.repository;

import com.pine.chat.chatRequest.model.ChatRequestEntity;
import com.pine.chat.chatRequest.model.ChatRequestStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRequestRepository extends JpaRepository<ChatRequestEntity, UUID> {

  List<ChatRequestEntity> findByReceiverIdANdStatus(UUID recieverId, ChatRequestStatus status);

  boolean existsBySenderIdAndReceiverId(UUID senderId, UUID receiverId);

}
