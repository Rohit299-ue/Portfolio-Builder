package com.portfolio.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    private String fullName;
    private String phone;
    private String title;
    
    @Column(length = 2000)
    private String bio;
    
    @Column(length = 2000)
    private String aboutText;
    
    private String location;
    private String linkedinUrl;
    private String githubUrl;
    
    @Column(length = 2000)
    private String skills;
    
    @Column(length = 5000)
    private String experience;
    
    @Column(length = 5000)
    private String education;
    
    private String resumeFileName;
    
    @Column(length = 10000)
    private String resumeContent;
    
    private String heroPhotoFileName;
    private String aboutPhotoFileName;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
