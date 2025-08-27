package com.you.require4testing.controller;

import com.you.require4testing.domain.TestRun;
import com.you.require4testing.repository.TestRunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/testruns")
@RequiredArgsConstructor
public class TestRunController {
    private final TestRunRepository repo;

    @PostMapping
    public TestRun create(@RequestBody TestRun tr) {
        tr.setCreatedAt(OffsetDateTime.now());
        return repo.save(tr);
    }

    @GetMapping
    public List<TestRun> list() {
        return repo.findAll();
    }
}