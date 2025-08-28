package com.you.require4testing.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {
    // Generate a secure key for JWT signing
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    // Set the token expiration time: 1 hour
    private static final long EXPIRATION = 1000 * 60 * 60; // 1 hour

    public static String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)  // Set the subject to the username
                .claim("role", role)   // Add role information to the payload
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))  // Set the expiration time
                .signWith(key)  // Sign with the key
                .compact();     // Generate the final JWT string
    }

    public static String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)  // Set the signing key
                .build()
                .parseClaimsJws(token)  // Parse and verify the JWT
                .getBody()  // Get the payload
                .getSubject();  // Get the subject (username)
    }

    public static String getRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);  // Get a custom role claim
    }
}
