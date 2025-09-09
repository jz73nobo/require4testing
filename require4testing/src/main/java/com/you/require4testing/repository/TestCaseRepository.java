package com.you.require4testing.repository;

import com.you.require4testing.domain.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
    List<TestCase> findByCreatedBy(Long createdBy);
    
    List<TestCase> findByRequirementId(Long requirementId);
    
    Optional<TestCase> findByTitleAndCreatedBy(String title, Long createdBy);
    
    @Query("SELECT tc FROM TestCase tc WHERE tc.requirement.id = :requirementId AND tc.createdBy = :userId")
    List<TestCase> findByRequirementAndUser(@Param("requirementId") Long requirementId, 
                                           @Param("userId") Long userId);
    
    long countByCreatedBy(Long createdBy);
}