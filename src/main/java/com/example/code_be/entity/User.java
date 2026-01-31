package com.example.code_be.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String pin; // Used as password

    @Column(nullable = true)
    private String email; // Added to match database schema

    private String avatar;

    @Column(name = "couple_start_date")
    private LocalDate coupleStartDate;

    @Column(name = "partner_id")
    private Long partnerId;
}
