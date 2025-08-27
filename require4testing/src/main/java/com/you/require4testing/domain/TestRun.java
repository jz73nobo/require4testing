package com.you.require4testing.domain;

import javax.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "test_runs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestRun {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    private Long createdBy;
    private OffsetDateTime createdAt;
}