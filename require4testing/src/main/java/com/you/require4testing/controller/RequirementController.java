package com.you.require4testing.controller;

import com.you.require4testing.domain.Requirement;
import com.you.require4testing.domain.User;
import com.you.require4testing.repository.RequirementRepository;
import com.you.require4testing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/requirements")
@RequiredArgsConstructor
public class RequirementController {
    private final RequirementRepository repo;
    private final UserRepository userRepository; // 添加用户仓库

    @PostMapping
    public Requirement create(@RequestBody Requirement req, Authentication authentication) {
        // 从认证信息中获取当前用户
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        
        // 通过用户名查询用户实体
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在: " + username));
        
        // 设置创建者和时间
        req.setCreatedBy(user.getId());
        req.setCreatedAt(OffsetDateTime.now());
        req.setUpdatedAt(OffsetDateTime.now()); // 添加更新时间
        
        return repo.save(req);
    }

    @GetMapping
    public List<Requirement> list(Authentication authentication) {
        // 从认证信息中获取当前用户
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        
        // 通过用户名查询用户实体
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User does not exist: " + username));
        
        // 只返回当前用户创建的需求
        return repo.findByCreatedBy(user.getId());
    }
}