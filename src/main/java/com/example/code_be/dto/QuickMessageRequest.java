package com.example.code_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Quick message request")
public class QuickMessageRequest {
    @Schema(description = "Message content", example = "Anh/Em yêu em/anh! ❤️")
    private String content;
}
