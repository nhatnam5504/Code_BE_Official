package com.example.code_be.dto;

import com.example.code_be.enums.Mood;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Set mood request")
public class MoodRequest {
    @Schema(description = "Mood to set")
    private Mood mood;

    @Schema(description = "Optional note about the mood", example = "Hôm nay rất vui!")
    private String note;
}
