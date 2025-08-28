package com.you.require4testing.config;

import com.you.require4testing.security.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

// 注意：使用 javax.servlet.* 而不是 jakarta.servlet.*
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable(); // Spring Boot 2 的写法

        // 添加JWT过滤器
        http.addFilterBefore(new JwtAuthenticationFilter(), BasicAuthenticationFilter.class);

        http.authorizeRequests() // Spring Boot 2 的写法
                .antMatchers("/api/auth/**").permitAll() // 使用 antMatchers 而不是 requestMatchers
                .anyRequest().authenticated();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // JWT过滤器类
    private static class JwtAuthenticationFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, 
                                      HttpServletResponse response, 
                                      FilterChain filterChain) throws ServletException, IOException {
            
            String authHeader = request.getHeader("Authorization");
            
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                try {
                    String username = JwtUtil.getUsername(token);
                    String role = JwtUtil.getRole(token);
                    
                    // 创建认证对象
                    var authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
                    var auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    
                } catch (Exception e) {
                    // JWT验证失败，清除认证信息
                    SecurityContextHolder.clearContext();
                }
            }
            
            filterChain.doFilter(request, response);
        }
    }
}