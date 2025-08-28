package com.you.require4testing.controller;

import com.you.require4testing.domain.TestCase;
import com.you.require4testing.domain.TestRun;
import com.you.require4testing.domain.TestRunAssignment;
import com.you.require4testing.repository.TestCaseRepository;
import com.you.require4testing.repository.TestRunAssignmentRepository;
import com.you.require4testing.repository.TestRunRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class TestRunAssignmentController {
    private final TestRunAssignmentRepository repo;
    private final TestRunRepository runRepo;
    private final TestCaseRepository caseRepo;

    @PostMapping
    public TestRunAssignment create(@RequestBody TestRunAssignment a) {
        // Verify test execution and test case existence
        TestRun tr = runRepo.findById(a.getTestRun().getId())
                .orElseThrow(() -> new RuntimeException("TestRun not found"));
        TestCase tc = caseRepo.findById(a.getTestCase().getId())
                .orElseThrow(() -> new RuntimeException("TestCase not found"));

        // Check for duplicate assignment
        boolean exists = repo.existsByTestRunAndTestCase(tr, tc);
        if (exists) {
            throw new RuntimeException("This TestCase already assigned to this TestRun");
        }
        
        a.setTestRun(tr);
        a.setTestCase(tc);
        a.setExecutedAt(OffsetDateTime.now());
        return repo.save(a);
    }

    @GetMapping
    public List<TestRunAssignment> list() {
        return repo.findAll();
    }

    // New update result method
    @PatchMapping("/{id}/result")
    public TestRunAssignment updateResult(@PathVariable Long id, @RequestBody ResultUpdateRequest req) {
        TestRunAssignment assignment = repo.findById(id).orElseThrow(() -> new RuntimeException("Assignment not found"));
        assignment.setResult(req.getResult());
        assignment.setComment(req.getComment());
        assignment.setExecutedAt(OffsetDateTime.now());
        return repo.save(assignment);
    }

    // DTO for update
    @Getter
    @Setter
    public static class ResultUpdateRequest {
        private String result;   // e.g. PASS, FAIL
        private String comment;
    }
}