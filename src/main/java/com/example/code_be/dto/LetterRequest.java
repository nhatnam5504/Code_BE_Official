package com.example.code_be.dto;

import com.example.code_be.enums.OpenType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Compose letter request")
public class LetterRequest {
    @Schema(description = "Letter content")
    private String content;

    @Schema(description = "Open type condition")
    private OpenType openType;

    @Schema(description = "Scheduled open datetime (yyyy-MM-ddTHH:mm:ss)", example = "2024-12-25T00:00:00")
    private String openAt;
}
