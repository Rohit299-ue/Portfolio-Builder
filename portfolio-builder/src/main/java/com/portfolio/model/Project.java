package com.portfolio.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "projects")
@Data
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    private String title;
    
    @Column(length = 1000)
    private String description;
    
    private String technologies;
    private String githubUrl;
    private String liveUrl;
    private Integer displayOrder;
}
