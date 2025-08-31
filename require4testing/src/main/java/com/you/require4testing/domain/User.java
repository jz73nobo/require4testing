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

    @Column(unique = true)
    private String username;
    
    private String password;
    private String fullName;
    private String role;
    private OffsetDateTime createdAt;
    
    // Automatically set creation time
    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }
}