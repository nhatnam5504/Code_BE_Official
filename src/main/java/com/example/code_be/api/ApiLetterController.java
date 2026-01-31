package com.example.code_be.api;

import com.example.code_be.dto.ApiResponse;
import com.example.code_be.dto.LetterRequest;
import com.example.code_be.entity.Letter;
import com.example.code_be.entity.User;
import com.example.code_be.enums.OpenType;
import com.example.code_be.service.LetterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/letters")
@RequiredArgsConstructor
@Tag(name = "Letters")
public class ApiLetterController {

    private final LetterService letterService;

    @GetMapping
    @Operation(summary = "Danh sách thư", description = "Lấy danh sách thư (inbox hoặc sent)")
    public ResponseEntity<ApiResponse<Map<String, Object>>> list(
            @Parameter(description = "inbox hoặc sent") @RequestParam(defaultValue = "inbox") String box,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Chưa đăng nhập"));
        }

        List<Letter> letters;
        if ("sent".equals(box)) {
            letters = letterService.findSent(user.getId());
        } else {
            letters = letterService.findInbox(user.getId());
        }

        Map<String, Object> data = new HashMap<>();
        data.put("letters", letters);
        data.put("box", box);
        data.put("userId", user.getId());

        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Chi tiết thư", description = "Lấy nội dung thư (nếu được phép mở)")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getById(
            @PathVariable Long id,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Chưa đăng nhập"));
        }

        Letter letter = letterService.findById(id);
        if (letter == null) {
            return ResponseEntity.notFound().build();
        }

        // Check access
        if (!letter.getFromUserId().equals(user.getId()) && !letter.getToUserId().equals(user.getId())) {
            return ResponseEntity.status(403).body(ApiResponse.error("Không có quyền truy cập"));
        }

        boolean canOpen = letterService.canOpen(letter, user.getId());
        boolean isRecipient = letter.getToUserId().equals(user.getId());
        boolean isSender = letter.getFromUserId().equals(user.getId());

        // Auto-open if can open and is recipient
        if (canOpen && !letter.getIsOpened() && isRecipient) {
            letter = letterService.openLetter(id);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("letter", letter);
        data.put("canOpen", canOpen);
        data.put("isRecipient", isRecipient);
        data.put("isSender", isSender);
        data.put("userId", user.getId());

        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @PostMapping
    @Operation(summary = "Gửi thư", description = "Viết và gửi thư bí mật cho người yêu")
    public ResponseEntity<ApiResponse<Letter>> compose(
            @RequestBody LetterRequest request,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        User partner = (User) session.getAttribute("partner");
        if (user == null || partner == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Chưa đăng nhập"));
        }

        Letter letter = Letter.builder()
                .fromUserId(user.getId())
                .toUserId(partner.getId())
                .content(request.getContent())
                .openType(request.getOpenType())
                .openAt(request.getOpenAt() != null && !request.getOpenAt().isEmpty()
                        ? LocalDateTime.parse(request.getOpenAt())
                        : null)
                .senderConfirmed(request.getOpenType() == OpenType.BOTH_CONFIRM)
                .build();

        Letter saved = letterService.save(letter);
        return ResponseEntity.ok(ApiResponse.success("Đã gửi thư! 💌", saved));
    }

    @PostMapping("/{id}/confirm")
    @Operation(summary = "Xác nhận mở thư", description = "Xác nhận cho phép mở thư (cần cả 2 người)")
    public ResponseEntity<ApiResponse<Letter>> confirmOpen(
            @PathVariable Long id,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Chưa đăng nhập"));
        }

        Letter letter = letterService.confirmOpen(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success("Đã xác nhận! Chờ người kia xác nhận nữa nhé 💕", letter));
    }

    @GetMapping("/enums")
    @Operation(summary = "Lấy OpenType enum", description = "Lấy danh sách loại mở thư")
    public ResponseEntity<ApiResponse<OpenType[]>> getEnums() {
        return ResponseEntity.ok(ApiResponse.success(OpenType.values()));
    }
}
