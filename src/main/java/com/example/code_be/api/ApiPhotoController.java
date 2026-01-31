package com.example.code_be.api;

import com.example.code_be.dto.ApiResponse;
import com.example.code_be.entity.Photo;
import com.example.code_be.service.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
@Tag(name = "Photos")
public class ApiPhotoController {

    private final PhotoService photoService;

    @GetMapping
    @Operation(summary = "Danh sách ảnh", description = "Lấy danh sách ảnh với phân trang, có thể lọc theo album")
    public ResponseEntity<ApiResponse<Map<String, Object>>> list(
            @Parameter(description = "Số trang (bắt đầu từ 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Lọc theo album") @RequestParam(required = false) String album) {

        Map<String, Object> data = new HashMap<>();
        List<String> albums = photoService.findAllAlbums();
        data.put("albums", albums);
        data.put("selectedAlbum", album);

        if (album != null && !album.isEmpty()) {
            data.put("photos", photoService.findByAlbum(album));
        } else {
            Page<Photo> photos = photoService.findAll(PageRequest.of(page, 20));
            data.put("photos", photos.getContent());
            data.put("currentPage", page);
            data.put("totalPages", photos.getTotalPages());
            data.put("totalElements", photos.getTotalElements());
        }

        return ResponseEntity.ok(ApiResponse.success(data));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Chi tiết ảnh", description = "Lấy thông tin chi tiết của một ảnh")
    public ResponseEntity<ApiResponse<Photo>> getById(@PathVariable Long id) {
        Photo photo = photoService.findById(id);
        if (photo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ApiResponse.success(photo));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload ảnh", description = "Upload ảnh mới với thông tin caption và album")
    public ResponseEntity<ApiResponse<Photo>> upload(
            @Parameter(description = "File ảnh") @RequestParam("file") MultipartFile file,
            @Parameter(description = "Caption") @RequestParam(required = false) String caption,
            @Parameter(description = "Album") @RequestParam(required = false) String album,
            @Parameter(description = "Album mới") @RequestParam(required = false) String newAlbum,
            @Parameter(description = "User ID") @RequestParam(defaultValue = "1") Long userId) {

        try {
            String url = photoService.uploadFile(file);
            String finalAlbum = (newAlbum != null && !newAlbum.isEmpty()) ? newAlbum : album;

            Photo photo = Photo.builder()
                    .uploaderId(userId)
                    .url(url)
                    .caption(caption)
                    .album(finalAlbum)
                    .takenAt(LocalDateTime.now())
                    .build();

            Photo saved = photoService.save(photo);
            return ResponseEntity.ok(ApiResponse.success("Upload thành công! 📸", saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Upload thất bại: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa ảnh", description = "Xóa một ảnh theo ID")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        photoService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Đã xóa ảnh!", null));
    }

    @GetMapping("/albums")
    @Operation(summary = "Danh sách album", description = "Lấy danh sách tất cả album")
    public ResponseEntity<ApiResponse<List<String>>> getAlbums() {
        return ResponseEntity.ok(ApiResponse.success(photoService.findAllAlbums()));
    }
}
