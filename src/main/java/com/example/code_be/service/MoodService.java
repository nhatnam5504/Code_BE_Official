package com.example.code_be.service;

import com.example.code_be.entity.DailyMood;
import com.example.code_be.enums.Mood;
import com.example.code_be.repository.DailyMoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MoodService {

    private final DailyMoodRepository dailyMoodRepository;

    public DailyMood setMood(Long userId, Mood mood, String note) {
        LocalDate today = LocalDate.now();
        Optional<DailyMood> existing = dailyMoodRepository.findByUserIdAndDate(userId, today);

        DailyMood dailyMood;
        if (existing.isPresent()) {
            dailyMood = existing.get();
            dailyMood.setMood(mood);
            dailyMood.setNote(note);
        } else {
            dailyMood = DailyMood.builder()
                    .userId(userId)
                    .mood(mood)
                    .note(note)
                    .date(today)
                    .build();
        }

        return dailyMoodRepository.save(dailyMood);
    }

    public List<DailyMood> getTodayMoods() {
        return dailyMoodRepository.findByDateOrderByUserIdAsc(LocalDate.now());
    }

    public Optional<DailyMood> getUserMoodToday(Long userId) {
        return dailyMoodRepository.findByUserIdAndDate(userId, LocalDate.now());
    }

    public List<DailyMood> getUserMoodHistory(Long userId) {
        return dailyMoodRepository.findByUserIdOrderByDateDesc(userId);
    }
}
