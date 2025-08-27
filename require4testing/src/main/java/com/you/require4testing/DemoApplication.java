package com.you.require4testing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// for bean data
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    // Adding a Password Encoder Bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }

    // Add CommandLineRunner to initialize data
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
}