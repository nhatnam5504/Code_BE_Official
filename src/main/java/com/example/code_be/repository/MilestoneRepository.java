package com.example.code_be.repository;

import com.example.code_be.entity.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, Long> {

    Optional<Milestone> findByTitle(String title);

    List<Milestone> findAllByOrderByDateDesc();

    List<Milestone> findAllByOrderByDateAsc();

    @Query(value = "SELECT * FROM milestones m WHERE EXTRACT(MONTH FROM m.date) = :month AND EXTRACT(DAY FROM m.date) = :day", nativeQuery = true)
    List<Milestone> findByMonthAndDay(int month, int day);
}
