package com.example.code_be.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "milestones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Milestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "photo_id")
    private Long photoId;

    private String icon;

    // Store multiple image URLs as comma-separated string
    @Column(columnDefinition = "TEXT")
    private String images;

    // Helper method to get images as list
    public List<String> getImageList() {
        if (images == null || images.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(images.split(","));
    }

    // Helper method to set images from list
    public void setImageList(List<String> imageList) {
        if (imageList == null || imageList.isEmpty()) {
            this.images = null;
        } else {
            this.images = String.join(",", imageList);
        }
    }
}
