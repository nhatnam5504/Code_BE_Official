package com.example.code_be.api;

import com.example.code_be.dto.ApiResponse;
import com.example.code_be.entity.*;
import com.example.code_be.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
@Tag(name = "Home")
public class ApiHomeController {

    private final MemoryService memoryService;
    private final PostService postService;
    private final MoodService moodService;
    private final QuickMessageService quickMessageService;
    private final MilestoneService milestoneService;

    @GetMapping("/dashboard")
    @Operation(summary = "Dashboard", description = "Lấy dữ liệu tổng hợp cho trang chủ")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboard() {
        LocalDate today = LocalDate.now();

        Map<String, Object> data = new HashMap<>();

        // Counts
        data.put("photoCount", 0); // Can add photo service later
        data.put("postCount", postService.count());
        data.put("milestoneCount", milestoneService.count());
        data.put("letterCount", 0); // Can add letter service later

        // Today's moods
        List<DailyMood> todayMoods = moodService.getTodayMoods();
        data.put("todayMoods", todayMoods);

        // Quick messages
        List<QuickMessage> quickMessages = quickMessageService.getActiveMessages(1L);
        if (!quickMessages.isEmpty()) {
            data.put("quickMessage", quickMessages.get(0));
        }

        // Recent milestones
        List<Milestone> milestones = milestoneService.findAllDesc();
        data.put("milestones", milestones.size() > 5 ? milestones.subList(0, 5) : milestones);

        // On This Day memories
        List<MemoryService.OnThisDayMemory> memories = memoryService.getOnThisDay(1L, today);
        data.put("todayMemories", memories);

        data.put("today", today.toString());

        return ResponseEntity.ok(ApiResponse.success(data));
    }
}
