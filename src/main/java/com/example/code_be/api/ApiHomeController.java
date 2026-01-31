package com.example.code_be.api;

import com.example.code_be.dto.ApiResponse;
import com.example.code_be.entity.*;
import com.example.code_be.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboard(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Chưa đăng nhập"));
        }

        User partner = (User) session.getAttribute("partner");
        LocalDate today = LocalDate.now();

        Map<String, Object> data = new HashMap<>();

        // On This Day memories
        List<MemoryService.OnThisDayMemory> memories = memoryService.getOnThisDay(user.getId(), today);
        data.put("memories", memories);

        // Days together
        if (user.getCoupleStartDate() != null) {
            long daysTogether = ChronoUnit.DAYS.between(user.getCoupleStartDate(), today);
            data.put("daysTogether", daysTogether);
            data.put("coupleStartDate", user.getCoupleStartDate().toString());
        }

        // Today's moods
        List<DailyMood> todayMoods = moodService.getTodayMoods();
        data.put("todayMoods", todayMoods);

        // Quick messages
        List<QuickMessage> quickMessages = quickMessageService.getActiveMessages(user.getId());
        data.put("quickMessages", quickMessages);

        // Recent milestones
        List<Milestone> milestones = milestoneService.findAllDesc();
        data.put("milestones", milestones.size() > 5 ? milestones.subList(0, 5) : milestones);

        data.put("user", user);
        data.put("partner", partner);
        data.put("today", today.toString());

        return ResponseEntity.ok(ApiResponse.success(data));
    }
}
