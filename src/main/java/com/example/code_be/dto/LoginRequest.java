package com.example.code_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Login request with username and password")
public class LoginRequest {
    @Schema(description = "Username", example = "nhatnam")
    private String username;

    @Schema(description = "Password", example = "050504")
    private String password;
}
