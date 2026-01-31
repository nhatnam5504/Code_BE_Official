package com.example.code_be.repository;

import com.example.code_be.entity.QuickMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuickMessageRepository extends JpaRepository<QuickMessage, Long> {

    Optional<QuickMessage> findByFromUserIdAndContent(Long fromUserId, String content);

    @Query("SELECT m FROM QuickMessage m WHERE m.toUserId = :userId AND m.expiresAt > :now ORDER BY m.createdAt DESC")
    List<QuickMessage> findActiveMessagesForUser(Long userId, LocalDateTime now);

    List<QuickMessage> findByToUserIdAndExpiresAtAfterOrderByCreatedAtDesc(Long toUserId, LocalDateTime now);
}
