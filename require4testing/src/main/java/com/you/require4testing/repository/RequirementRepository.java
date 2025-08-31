package com.you.require4testing.repository;

import com.you.require4testing.domain.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RequirementRepository extends JpaRepository<Requirement, Long> {
    // 添加按创建者查询的方法
    List<Requirement> findByCreatedBy(Long createdBy);
}