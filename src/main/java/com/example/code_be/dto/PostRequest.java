package com.example.code_be.dto;

import com.example.code_be.enums.Mood;
import com.example.code_be.enums.Visibility;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Create/update post request")
public class PostRequest {
    @Schema(description = "Post title", example = "Ngày đầu tiên gặp nhau")
    private String title;

    @Schema(description = "Post content")
    private String content;

    @Schema(description = "Mood of the post")
    private Mood mood;

    @Schema(description = "Visibility setting")
    private Visibility visibility;

    @Schema(description = "Date when event occurred (yyyy-MM-dd)", example = "2024-01-15")
    private String occurredAt;
}
