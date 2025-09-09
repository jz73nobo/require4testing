package com.you.require4testing.controller;

import com.you.require4testing.domain.Requirement;
import com.you.require4testing.domain.TestCase;
import com.you.require4testing.domain.User;
import com.you.require4testing.repository.RequirementRepository;
import com.you.require4testing.repository.TestCaseRepository;
import com.you.require4testing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/testcases")
@RequiredArgsConstructor
public class TestCaseController {
    private final TestCaseRepository repo;
    private final RequirementRepository reqRepo;
    private final UserRepository userRepository; // 添加UserRepository

    @PostMapping
    public TestCase create(@RequestBody TestCase tc, Authentication authentication) {
        // 从认证信息中获取当前用户
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        
        // 通过用户名查询用户实体
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
        // 验证需求是否存在
        Requirement requirement = reqRepo.findById(tc.getRequirement().getId())
                .orElseThrow(() -> new RuntimeException("Requirement not found"));
        
        // 设置测试用例属性
        tc.setRequirement(requirement);
        tc.setCreatedBy(user.getId()); // 设置创建者ID
        tc.setCreatedAt(OffsetDateTime.now());
        
        return repo.save(tc);
    }

    @GetMapping
    public List<TestCase> list(Authentication authentication) {
        // 从认证信息中获取当前用户
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        
        // 通过用户名查询用户实体
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
        // 只返回当前用户创建的测试用例
        return repo.findByCreatedBy(user.getId());
    }
}