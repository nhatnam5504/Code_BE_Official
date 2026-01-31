package com.example.code_be.service;

import com.example.code_be.entity.Milestone;
import com.example.code_be.repository.MilestoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MilestoneService {

    private final MilestoneRepository milestoneRepository;

    public Milestone save(Milestone milestone) {
        return milestoneRepository.save(milestone);
    }

    public Milestone findById(Long id) {
        return milestoneRepository.findById(id).orElse(null);
    }

    public List<Milestone> findAllAsc() {
        return milestoneRepository.findAllByOrderByDateAsc();
    }

    public List<Milestone> findAllDesc() {
        return milestoneRepository.findAllByOrderByDateDesc();
    }

    public void delete(Long id) {
        milestoneRepository.deleteById(id);
    }

    public long count() {
        return milestoneRepository.count();
    }

    public List<Milestone> findOnThisDay(int month, int day) {
        return milestoneRepository.findByMonthAndDay(month, day);
    }
}
