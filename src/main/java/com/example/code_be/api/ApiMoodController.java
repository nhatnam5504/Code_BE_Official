package com.example.code_be.api;

import com.example.code_be.dto.ApiResponse;
import com.example.code_be.dto.MoodRequest;
import com.example.code_be.entity.DailyMood;
import com.example.code_be.entity.User;
import com.example.code_be.enums.Mood;
import com.example.code_be.service.MoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mood")
@RequiredArgsConstructor
@Tag(name = "Mood")
public class ApiMoodController {

    private final MoodService moodService;

    @GetMapping("/today")
    @Operation(summary = "Cảm xúc hôm nay", description = "Lấy cảm xúc hôm nay của cả 2 người")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTodayMoods(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Chưa đăng nhập"));
        }

        List<DailyMood> todayMoods = moodService.getTodayMoods();
        DailyMood myMood = moodService.getUserMoodToday(user.getId()).orElse(null);

        Map<String, Object> data = new HashMap<>();
        data.put("todayMoods", todayMoods);
        data.put("myMood", myMood);
        data.put("userId", user.getId());
        data.put("moods", Mood.values());

        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @PostMapping
    @Operation(summary = "Đặt cảm xúc", description = "Cập nhật cảm xúc hôm nay")
    public ResponseEntity<ApiResponse<DailyMood>> setMood(
            @RequestBody MoodRequest request,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Chưa đăng nhập"));
        }

        DailyMood mood = moodService.setMood(user.getId(), request.getMood(), request.getNote());
        return ResponseEntity.ok(ApiResponse.success("Đã lưu cảm xúc! " + request.getMood().getEmoji(), mood));
    }

    @GetMapping("/history")
    @Operation(summary = "Lịch sử cảm xúc", description = "Lấy lịch sử cảm xúc của người dùng")
    public ResponseEntity<ApiResponse<List<DailyMood>>> getHistory(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Chưa đăng nhập"));
        }

        List<DailyMood> history = moodService.getUserMoodHistory(user.getId());
        return ResponseEntity.ok(ApiResponse.success(history));
    }

    @GetMapping("/enums")
    @Operation(summary = "Lấy Mood enum", description = "Lấy danh sách các loại cảm xúc")
    public ResponseEntity<ApiResponse<Mood[]>> getEnums() {
        return ResponseEntity.ok(ApiResponse.success(Mood.values()));
    }
}
