package com.example.code_be.api;

import com.example.code_be.dto.ApiResponse;
import com.example.code_be.dto.LoginRequest;
import com.example.code_be.entity.User;
import com.example.code_be.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth")
public class ApiAuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Đăng nhập", description = "Xác thực người dùng bằng username và password")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(
            @RequestBody LoginRequest request,
            HttpSession session) {

        return authService.login(request.getUsername(), request.getPassword())
                .map(user -> {
                    session.setAttribute("user", user);
                    session.setAttribute("partner", authService.getPartner(user.getId()));

                    Map<String, Object> data = new HashMap<>();
                    data.put("user", user);
                    data.put("partner", authService.getPartner(user.getId()));

                    return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công!", data));
                })
                .orElseGet(() -> ResponseEntity.badRequest()
                        .body(ApiResponse.error("Tên đăng nhập hoặc mật khẩu không đúng!")));
    }

    @PostMapping("/logout")
    @Operation(summary = "Đăng xuất", description = "Xóa session và đăng xuất người dùng")
    public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(ApiResponse.success("Đã đăng xuất!", null));
    }

    @GetMapping("/me")
    @Operation(summary = "Thông tin người dùng", description = "Lấy thông tin người dùng đang đăng nhập")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Chưa đăng nhập"));
        }

        User partner = (User) session.getAttribute("partner");
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("partner", partner);

        return ResponseEntity.ok(ApiResponse.success(data));
    }
}
