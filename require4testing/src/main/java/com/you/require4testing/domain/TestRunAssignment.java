package com.you.require4testing.domain;

import javax.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "test_run_assignments",
       uniqueConstraints = @UniqueConstraint(columnNames = {"test_run_id", "test_case_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestRunAssignment {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "test_run_id")
    private TestRun testRun;

    @ManyToOne
    @JoinColumn(name = "test_case_id")
    private TestCase testCase;

    private Long assignedTesterId;

    private String result;
    private String comment;
    private OffsetDateTime executedAt;
}