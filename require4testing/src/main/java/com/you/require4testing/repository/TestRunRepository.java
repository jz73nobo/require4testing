package com.you.require4testing.repository;

import com.you.require4testing.domain.TestRun;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRunRepository extends JpaRepository<TestRun, Long> {
}