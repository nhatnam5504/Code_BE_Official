package com.example.code_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "Create/update milestone request")
public class MilestoneRequest {
    @Schema(description = "Milestone title", example = "Lần đầu hẹn hò")
    private String title;

    @Schema(description = "Milestone description")
    private String description;

    @Schema(description = "Milestone date (yyyy-MM-dd)", example = "2024-01-15")
    private String date;

    @Schema(description = "Milestone icon emoji", example = "💕")
    private String icon;

    @Schema(description = "List of image URLs")
    private List<String> images;
}
