package com.example.code_be.service;

import com.example.code_be.entity.*;
import lombok.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MemoryService {

    private final PhotoService photoService;
    private final PostService postService;
    private final MilestoneService milestoneService;

    @Data
    @AllArgsConstructor
    public static class OnThisDayMemory {
        private int year;
        private List<Photo> photos;
        private List<Post> posts;
        private List<Milestone> milestones;

        public boolean hasMemories() {
            return !photos.isEmpty() || !posts.isEmpty() || !milestones.isEmpty();
        }
    }

    public List<OnThisDayMemory> getOnThisDay(Long userId, LocalDate date) {
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        int currentYear = date.getYear();

        List<Photo> photos = photoService.findOnThisDay(month, day);
        List<Post> posts = postService.findOnThisDay(userId, month, day);
        List<Milestone> milestones = milestoneService.findOnThisDay(month, day);

        // Group by year
        Map<Integer, OnThisDayMemory> memoryByYear = new TreeMap<>(Collections.reverseOrder());

        for (Photo photo : photos) {
            if (photo.getTakenAt() != null) {
                int year = photo.getTakenAt().getYear();
                if (year < currentYear) {
                    memoryByYear
                            .computeIfAbsent(year,
                                    y -> new OnThisDayMemory(y, new ArrayList<>(), new ArrayList<>(),
                                            new ArrayList<>()))
                            .getPhotos().add(photo);
                }
            }
        }

        for (Post post : posts) {
            if (post.getOccurredAt() != null) {
                int year = post.getOccurredAt().getYear();
                if (year < currentYear) {
                    memoryByYear
                            .computeIfAbsent(year,
                                    y -> new OnThisDayMemory(y, new ArrayList<>(), new ArrayList<>(),
                                            new ArrayList<>()))
                            .getPosts().add(post);
                }
            }
        }

        for (Milestone milestone : milestones) {
            int year = milestone.getDate().getYear();
            if (year < currentYear) {
                memoryByYear
                        .computeIfAbsent(year,
                                y -> new OnThisDayMemory(y, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()))
                        .getMilestones().add(milestone);
            }
        }

        return new ArrayList<>(memoryByYear.values());
    }
}
