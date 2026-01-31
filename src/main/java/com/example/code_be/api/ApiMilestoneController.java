package com.example.code_be.api;

import com.example.code_be.dto.ApiResponse;
import com.example.code_be.dto.MilestoneRequest;
import com.example.code_be.entity.Milestone;
import com.example.code_be.entity.User;
import com.example.code_be.service.MilestoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/milestones")
@RequiredArgsConstructor
@Tag(name = "Milestones")
public class ApiMilestoneController {

    private final MilestoneService milestoneService;

    @GetMapping
    @Operation(summary = "Danh sách mốc kỷ niệm", description = "Lấy danh sách tất cả mốc kỷ niệm theo thứ tự thời gian")
    public ResponseEntity<ApiResponse<List<Milestone>>> list(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Chưa đăng nhập"));
        }

        List<Milestone> milestones = milestoneService.findAllAsc();
        return ResponseEntity.ok(ApiResponse.success(milestones));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Chi tiết mốc kỷ niệm", description = "Lấy thông tin một mốc kỷ niệm")
    public ResponseEntity<ApiResponse<Milestone>> getById(
            @PathVariable Long id,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Chưa đăng nhập"));
        }

        Milestone milestone = milestoneService.findById(id);
        if (milestone == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ApiResponse.success(milestone));
    }

    @PostMapping
    @Operation(summary = "Tạo mốc kỷ niệm", description = "Thêm mốc kỷ niệm mới")
    public ResponseEntity<ApiResponse<Milestone>> create(
            @RequestBody MilestoneRequest request,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Chưa đăng nhập"));
        }

        Milestone milestone = Milestone.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .date(LocalDate.parse(request.getDate()))
                .icon(request.getIcon() != null && !request.getIcon().isEmpty() ? request.getIcon() : "💕")
                .images(request.getImages() != null ? String.join(",", request.getImages()) : null)
                .build();

        Milestone saved = milestoneService.save(milestone);
        return ResponseEntity.ok(ApiResponse.success("Đã thêm mốc kỷ niệm! 🎉", saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật mốc kỷ niệm", description = "Sửa thông tin mốc kỷ niệm")
    public ResponseEntity<ApiResponse<Milestone>> update(
            @PathVariable Long id,
            @RequestBody MilestoneRequest request,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Chưa đăng nhập"));
        }

        Milestone milestone = milestoneService.findById(id);
        if (milestone == null) {
            return ResponseEntity.notFound().build();
        }

        milestone.setTitle(request.getTitle());
        milestone.setDescription(request.getDescription());
        milestone.setDate(LocalDate.parse(request.getDate()));
        milestone.setIcon(request.getIcon() != null && !request.getIcon().isEmpty() ? request.getIcon() : "💕");
        if (request.getImages() != null) {
            milestone.setImages(String.join(",", request.getImages()));
        }

        Milestone saved = milestoneService.save(milestone);
        return ResponseEntity.ok(ApiResponse.success("Đã cập nhật!", saved));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa mốc kỷ niệm", description = "Xóa một mốc kỷ niệm")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Chưa đăng nhập"));
        }

        milestoneService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Đã xóa mốc kỷ niệm!", null));
    }
}
