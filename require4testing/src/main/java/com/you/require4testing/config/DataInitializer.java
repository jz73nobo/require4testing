// DataInitializer.java
package com.you.require4testing.config;

import com.you.require4testing.domain.User;
import com.you.require4testing.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    
    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            System.out.println("🔄 正在初始化用户密码...");
            
            // 重置 admin 用户密码
            userRepository.findByUsername("admin").ifPresent(user -> {
                String newHash = encoder.encode("password");
                user.setPassword(newHash);
                userRepository.save(user);
                System.out.println("✅ 重置 admin 用户密码为 'password'");
            });
            
            System.out.println("🎉 用户密码初始化完成");
        };
    }
}