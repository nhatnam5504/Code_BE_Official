package com.example.code_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Photo upload/update request")
public class PhotoRequest {
    @Schema(description = "Photo caption", example = "Kỷ niệm đẹp ❤️")
    private String caption;

    @Schema(description = "Album name", example = "Dating")
    private String album;

    @Schema(description = "New album name (if creating new album)")
    private String newAlbum;
}
