package com.you.require4testing.controller;

import com.you.require4testing.domain.TestRun;
import com.you.require4testing.domain.User;
import com.you.require4testing.repository.TestRunRepository;
import com.you.require4testing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/testruns")
@RequiredArgsConstructor
public class TestRunController {
    private final TestRunRepository repo;
    private final UserRepository userRepository; // 添加UserRepository

    @PostMapping
    public TestRun create(@RequestBody TestRun tr, Authentication authentication) {
        // 从认证信息中获取当前用户
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        
        // 通过用户名查询用户实体
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
        tr.setCreatedBy(user.getId()); // 设置创建者ID
        tr.setCreatedAt(OffsetDateTime.now());
        return repo.save(tr);
    }

    @GetMapping
    public List<TestRun> list(Authentication authentication) {
        // 从认证信息中获取当前用户
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        
        // 通过用户名查询用户实体
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
        // 只返回当前用户创建的测试运行
        return repo.findByCreatedBy(user.getId());
    }
}