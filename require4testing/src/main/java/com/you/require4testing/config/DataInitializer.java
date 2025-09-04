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
            System.out.println("🔄 Checking and resetting all user passwords...");
            
            // 所有需要重置密码的用户列表
            String[] usersToReset = {
                "admin", 
                "req_engineer", 
                "test_manager", 
                "test_designer", 
                "tester1",
                "tester2"
            };
            
            int resetCount = 0;
            
            for (String username : usersToReset) {
                userRepository.findByUsername(username).ifPresent(user -> {
                    String oldHash = user.getPassword();
                    
                    // 检查当前密码是否能匹配 "password"
                    if (!encoder.matches("password", oldHash)) {
                        String newHash = encoder.encode("password");
                        user.setPassword(newHash);
                        userRepository.save(user);
                        System.out.println("✅ Reset User '" + username + "' Password is 'password'");
                    } else {
                        System.out.println("ℹ️  User '" + username + "' Password is already 'password'，skip reset");
                    }
                });
            }
            
            System.out.println("🎉 all check done");
        };
    }
}