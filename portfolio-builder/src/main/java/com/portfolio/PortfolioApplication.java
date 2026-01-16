package com.portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PortfolioApplication {
    public static void main(String[] args) {
        // Create uploads directory if it doesn't exist
        String uploadDir = System.getProperty("user.dir") + "/uploads";
        java.io.File uploadsDirectory = new java.io.File(uploadDir);
        if (!uploadsDirectory.exists()) {
            boolean created = uploadsDirectory.mkdirs();
            if (created) {
                System.out.println("✓ Created uploads directory at: " + uploadDir);
            }
        } else {
            System.out.println("✓ Uploads directory exists at: " + uploadDir);
        }
        
        // Create data directory for database if it doesn't exist
        String dataDir = System.getProperty("user.dir") + "/data";
        java.io.File dataDirectory = new java.io.File(dataDir);
        if (!dataDirectory.exists()) {
            boolean created = dataDirectory.mkdirs();
            if (created) {
                System.out.println("✓ Created database directory at: " + dataDir);
            }
        } else {
            System.out.println("✓ Database directory exists at: " + dataDir);
        }
        
        SpringApplication.run(PortfolioApplication.class, args);
    }
}
