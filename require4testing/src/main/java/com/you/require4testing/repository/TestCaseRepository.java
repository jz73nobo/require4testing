package com.you.require4testing.repository;

import com.you.require4testing.domain.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
    List<TestCase> findByCreatedBy(Long createdBy);
}