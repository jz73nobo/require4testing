package com.you.require4testing.controller;

import com.you.require4testing.domain.Requirement;
import com.you.require4testing.repository.RequirementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/requirements")
@RequiredArgsConstructor
public class RequirementController {
    private final RequirementRepository repo;

    @PostMapping
    public Requirement create(@RequestBody Requirement req) {
        req.setCreatedAt(OffsetDateTime.now());
        return repo.save(req);
    }

    @GetMapping
    public List<Requirement> list() {
        return repo.findAll();
    }
}
