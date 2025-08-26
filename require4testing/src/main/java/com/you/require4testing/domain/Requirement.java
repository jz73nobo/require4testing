package com.you.require4testing.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "requirements")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Requirement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Long createdBy;

    private OffsetDateTime createdAt;
}
