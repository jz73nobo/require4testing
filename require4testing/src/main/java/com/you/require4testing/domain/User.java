package com.you.require4testing.domain;

import javax.persistence.*; 
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "users")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class User {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    private String fullName;

    // e.g. "TEST_MANAGER", "TESTER", "AUTHOR"
    @Column(nullable = false)
    private String role;

    private OffsetDateTime createdAt;
    
    // Automatically set creation time
    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }
}