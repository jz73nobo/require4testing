package com.you.require4testing;
import com.you.require4testing.repository.UserRepository;
import com.you.require4testing.domain.User;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// for bean data
// import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;



import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
    // 已注释：PasswordEncoder 现在在 SecurityConfig.java 中定义
    /*
    // Adding a Password Encoder Bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }
    */

    // Add CommandLineRunner to initialize data
    @Bean
    CommandLineRunner seedUsers(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (!repo.findByUsername("admin").isPresent()) {
                repo.save(User.builder()
                        .username("admin")
                        .password(encoder.encode("password"))
                        .fullName("Admin Manager")
                        .role("TEST_MANAGER")
                        .build());
            }
            if (!repo.findByUsername("tester1").isPresent()) {
                repo.save(User.builder()
                        .username("tester1")
                        .password(encoder.encode("testpass"))
                        .fullName("Tester One")
                        .role("TESTER")
                        .build());
            }
        };
    }
    /*
    @Bean
    CommandLineRunner seedUsers(JdbcTemplate jdbc, PasswordEncoder encoder) {
        return args -> {
            // Check if the admin user already exists
            Integer exists = jdbc.queryForObject(
                "SELECT COUNT(*) FROM users WHERE username='admin'", 
                Integer.class
            );
            
            // If the user does not exist, create the admin user.
            if (exists != null && exists == 0) {
                jdbc.update(
                    "INSERT INTO users(username, password, full_name, role) VALUES (?, ?, ?, ?)",
                    "admin", 
                    encoder.encode("adminpass"), // Passwords will be stored encrypted
                    "Admin", 
                    "TEST_MANAGER"
                );
                System.out.println("Administrator user created: admin/adminpass");
            }
        };
    }
    */
}