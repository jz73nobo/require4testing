package com.you.require4testing.controller;

import com.you.require4testing.domain.User;
import com.you.require4testing.repository.UserRepository;
import com.you.require4testing.security.JwtUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

// import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    // user data access layer
    private final UserRepository userRepo;
    // password encryptor to verify password
    private final PasswordEncoder encoder;

    /**
    * User login interface
    * Receives username and password, and returns a JWT Token after verification
    */

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest req) {
        User user = userRepo.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = JwtUtil.generateToken(user.getUsername(), user.getRole());

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("role", user.getRole());
        return response;  // 直接返回 JSON
    }


    /*
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest req) {
        // 1. Find users by username
        User user = userRepo.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // 2. Verify password: compare the plaintext password with the encrypted password in the database 
        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // 3. Verification successful, generate JWT Token
        String token = JwtUtil.generateToken(user.getUsername(), user.getRole());
        
        // 4. Return Token and role information to the client
        return Map.of("token", token, "role", user.getRole());
    }
    */

    /**
    * Data transfer object for login requests
    * Used to receive the username and password from the front-end
    */
    @Getter 
    @Setter
    static class LoginRequest {
        private String username;
        private String password;
    }
}
