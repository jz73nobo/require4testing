package com.you.require4testing.repository;

import com.you.require4testing.domain.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
}