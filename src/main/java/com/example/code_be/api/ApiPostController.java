package com.example.code_be.api;

import com.example.code_be.dto.ApiResponse;
import com.example.code_be.dto.PostRequest;
import com.example.code_be.entity.Post;
import com.example.code_be.enums.Mood;
import com.example.code_be.enums.Visibility;
import com.example.code_be.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Posts")
public class ApiPostController {

    private final PostService postService;

    @GetMapping
    @Operation(summary = "Danh sách bài viết", description = "Lấy danh sách bài viết với phân trang")
    public ResponseEntity<ApiResponse<Map<String, Object>>> list(
            @Parameter(description = "Số trang") @RequestParam(defaultValue = "0") int page) {

        Page<Post> posts = postService.findAll(PageRequest.of(page, 10));

        Map<String, Object> data = new HashMap<>();
        data.put("posts", posts.getContent());
        data.put("currentPage", page);
        data.put("totalPages", posts.getTotalPages());
        data.put("totalElements", posts.getTotalElements());

        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Chi tiết bài viết", description = "Lấy thông tin chi tiết bài viết")
    public ResponseEntity<ApiResponse<Post>> getById(@PathVariable Long id) {
        Post post = postService.findById(id);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResponse.success(post));
    }

    @PostMapping
    @Operation(summary = "Tạo bài viết", description = "Tạo bài viết mới")
    public ResponseEntity<ApiResponse<Post>> create(
            @RequestBody PostRequest request,
            @RequestParam(defaultValue = "1") Long userId) {

        Post post = Post.builder()
                .ownerId(userId)
                .title(request.getTitle())
                .content(request.getContent())
                .mood(request.getMood())
                .visibility(request.getVisibility())
                .occurredAt(request.getOccurredAt() != null && !request.getOccurredAt().isEmpty()
                        ? LocalDate.parse(request.getOccurredAt())
                        : LocalDate.now())
                .build();

        Post saved = postService.save(post);
        return ResponseEntity.ok(ApiResponse.success("Đã lưu bài viết! 📝", saved));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật bài viết", description = "Cập nhật bài viết theo ID")
    public ResponseEntity<ApiResponse<Post>> update(
            @PathVariable Long id,
            @RequestBody PostRequest request) {

        Post post = postService.findById(id);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setMood(request.getMood());
        post.setVisibility(request.getVisibility());

        Post saved = postService.save(post);
        return ResponseEntity.ok(ApiResponse.success("Đã cập nhật!", saved));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa bài viết", description = "Xóa bài viết theo ID")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        Post post = postService.findById(id);
        if (post != null) {
            postService.delete(id);
            return ResponseEntity.ok(ApiResponse.success("Đã xóa bài viết!", null));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/enums")
    @Operation(summary = "Lấy enum values", description = "Lấy danh sách Mood và Visibility")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getEnums() {
        Map<String, Object> data = new HashMap<>();
        data.put("moods", Mood.values());
        data.put("visibilities", Visibility.values());
        return ResponseEntity.ok(ApiResponse.success(data));
    }
}
