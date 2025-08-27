package com.you.require4testing.controller;

import com.you.require4testing.domain.Requirement;
import com.you.require4testing.domain.TestCase;
import com.you.require4testing.repository.RequirementRepository;
import com.you.require4testing.repository.TestCaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/testcases")
@RequiredArgsConstructor
public class TestCaseController {
    private final TestCaseRepository repo;
    private final RequirementRepository reqRepo;

    @PostMapping
    public TestCase create(@RequestBody TestCase tc) {
        Requirement r = reqRepo.findById(tc.getRequirement().getId())
                .orElseThrow(() -> new RuntimeException("Requirement not found"));
        tc.setRequirement(r);
        tc.setCreatedAt(OffsetDateTime.now());
        return repo.save(tc);
    }

    @GetMapping
    public List<TestCase> list() {
        return repo.findAll();
    }
}
