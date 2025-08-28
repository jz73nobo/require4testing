package com.you.require4testing.repository;

import com.you.require4testing.domain.TestCase;
import com.you.require4testing.domain.TestRun;
import com.you.require4testing.domain.TestRunAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRunAssignmentRepository extends JpaRepository<TestRunAssignment, Long> {
    boolean existsByTestRunAndTestCase(TestRun testRun, TestCase testCase);
}