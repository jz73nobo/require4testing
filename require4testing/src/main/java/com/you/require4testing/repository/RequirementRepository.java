package com.you.require4testing.repository;

import com.you.require4testing.domain.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RequirementRepository extends JpaRepository<Requirement, Long> {}
