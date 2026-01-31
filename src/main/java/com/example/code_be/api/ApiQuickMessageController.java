package com.example.code_be.api;

import com.example.code_be.dto.ApiResponse;
import com.example.code_be.dto.QuickMessageRequest;
import com.example.code_be.entity.QuickMessage;
import com.example.code_be.entity.User;
import com.example.code_be.service.QuickMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quick")
@RequiredArgsConstructor
@Tag(name = "Quick Messages")
public class ApiQuickMessageController {

    private final QuickMessageService quickMessageService;

    @PostMapping
    @Operation(summary = "Gửi tin nhắn nhanh", description = "Gửi tin nhắn nhanh cho người yêu")
    public ResponseEntity<ApiResponse<QuickMessage>> send(
            @RequestBody QuickMessageRequest request,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        User partner = (User) session.getAttribute("partner");
        if (user == null || partner == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Chưa đăng nhập"));
        }

        QuickMessage message = quickMessageService.send(user.getId(), partner.getId(), request.getContent());
        return ResponseEntity.ok(ApiResponse.success("Đã gửi lời nhắn! 💬", message));
    }

    @GetMapping("/active")
    @Operation(summary = "Tin nhắn đang hoạt động", description = "Lấy danh sách tin nhắn nhanh chưa đọc")
    public ResponseEntity<ApiResponse<List<QuickMessage>>> getActive(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Chưa đăng nhập"));
        }

        List<QuickMessage> messages = quickMessageService.getActiveMessages(user.getId());
        return ResponseEntity.ok(ApiResponse.success(messages));
    }

    @PostMapping("/{id}/read")
    @Operation(summary = "Đánh dấu đã đọc", description = "Đánh dấu tin nhắn là đã đọc")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @PathVariable Long id,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Chưa đăng nhập"));
        }

        quickMessageService

                .markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success("Đã đọc!", null));
    }
}
