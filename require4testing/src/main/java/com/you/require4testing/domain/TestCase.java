package com.you.require4testing.domain;

import javax.persistence.*; // Note: Spring Boot 2.x uses javax.persistence, not jakarta.persistence
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "test_cases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestCase {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requirement_id")
    private Requirement requirement;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Long createdBy;

    private OffsetDateTime createdAt;
}