package com.example.code_be.repository;

import com.example.code_be.entity.DailyMood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyMoodRepository extends JpaRepository<DailyMood, Long> {

    Optional<DailyMood> findByUserIdAndDate(Long userId, LocalDate date);

    List<DailyMood> findByDateOrderByUserIdAsc(LocalDate date);

    List<DailyMood> findByUserIdOrderByDateDesc(Long userId);
}
