package com.you.require4testing.controller;

import com.you.require4testing.domain.Requirement;
import com.you.require4testing.domain.TestCase;
import com.you.require4testing.domain.User;
import com.you.require4testing.repository.RequirementRepository;
import com.you.require4testing.repository.TestCaseRepository;
import com.you.require4testing.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/testcases")
@RequiredArgsConstructor
public class TestCaseController {
    private final TestCaseRepository repo;
    private final RequirementRepository reqRepo;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TestCaseRequest request, Authentication authentication) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));
            
            TestCase tc = new TestCase();
            tc.setTitle(request.getTitle());
            tc.setDescription(request.getDescription());
            tc.setCreatedBy(user.getId());
            tc.setCreatedAt(OffsetDateTime.now());
            
            TestCase savedTestCase = repo.save(tc);
            return ResponseEntity.ok(savedTestCase);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> list(Authentication authentication) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));
            
            List<TestCase> testCases = repo.findByCreatedBy(user.getId());
            return ResponseEntity.ok(testCases);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, Authentication authentication) {
        try {
            Optional<TestCase> testCase = repo.findById(id);
            if (testCase.isPresent()) {
                return ResponseEntity.ok(testCase.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody TestCaseRequest request, Authentication authentication) {
        try {
            Optional<TestCase> existingTestCase = repo.findById(id);
            if (!existingTestCase.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            TestCase testCase = existingTestCase.get();
            testCase.setTitle(request.getTitle());
            testCase.setDescription(request.getDescription());

            if (request.getRequirementId() != null) {
                Requirement requirement = reqRepo.findById(request.getRequirementId())
                        .orElseThrow(() -> new RuntimeException("Requirement not found"));
                testCase.setRequirement(requirement);
            }

            TestCase updatedTestCase = repo.save(testCase);
            return ResponseEntity.ok(updatedTestCase);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            if (repo.existsById(id)) {
                repo.deleteById(id);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Getter
    @Setter
    public static class TestCaseRequest {
        private String title;
        private String description;
        private Long requirementId;
    }
}