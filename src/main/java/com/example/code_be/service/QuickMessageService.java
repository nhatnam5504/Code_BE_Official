package com.example.code_be.service;

import com.example.code_be.entity.QuickMessage;
import com.example.code_be.repository.QuickMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuickMessageService {

    private final QuickMessageRepository quickMessageRepository;

    public QuickMessage send(Long fromUserId, Long toUserId, String content) {
        QuickMessage message = QuickMessage.builder()
                .fromUserId(fromUserId)
                .toUserId(toUserId)
                .content(content)
                .build();
        return quickMessageRepository.save(message);
    }

    public List<QuickMessage> getActiveMessages(Long userId) {
        return quickMessageRepository.findActiveMessagesForUser(userId, LocalDateTime.now());
    }

    public void markAsRead(Long messageId) {
        quickMessageRepository.findById(messageId).ifPresent(message -> {
            message.setIsRead(true);
            quickMessageRepository.save(message);
        });
    }
}
