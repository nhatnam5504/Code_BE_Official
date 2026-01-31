package com.example.code_be.entity;

import com.example.code_be.enums.Mood;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "daily_moods")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyMood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Mood mood;

    private String note;

    @Column(nullable = false)
    private LocalDate date;
}
