package com.example.code_be.entity;

import com.example.code_be.enums.OpenType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "letters")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Letter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_user_id", nullable = false)
    private Long fromUserId;

    @Column(name = "to_user_id", nullable = false)
    private Long toUserId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "open_type", nullable = false)
    private OpenType openType;

    @Column(name = "open_at")
    private LocalDateTime openAt;

    @Column(name = "sender_confirmed")
    @Builder.Default
    private Boolean senderConfirmed = false;

    @Column(name = "receiver_confirmed")
    @Builder.Default
    private Boolean receiverConfirmed = false;

    @Column(name = "is_opened")
    @Builder.Default
    private Boolean isOpened = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (openType == OpenType.NOW) {
            isOpened = true;
        }
    }

    public boolean canOpen(Long userId) {
        if (isOpened)
            return true;

        switch (openType) {
            case NOW:
                return true;
            case SCHEDULED:
                return openAt != null && LocalDateTime.now().isAfter(openAt);
            case BOTH_CONFIRM:
                return senderConfirmed && receiverConfirmed;
            default:
                return false;
        }
    }
}
