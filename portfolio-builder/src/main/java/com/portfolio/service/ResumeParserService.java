package com.portfolio.service;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class ResumeParserService {

    private final Tika tika = new Tika();

    public String parseResume(MultipartFile file) throws IOException {
        try {
            return tika.parseToString(file.getInputStream());
        } catch (TikaException e) {
            throw new IOException("Failed to parse resume", e);
        }
    }

    public String extractSkills(String resumeContent) {
        String[] commonSkills = {"Java", "Spring", "JavaScript", "Python", "React", "Angular", 
                                "Node.js", "SQL", "MongoDB", "AWS", "Docker", "Kubernetes",
                                "HTML", "CSS", "Git", "REST API", "Microservices", "TypeScript",
                                "Vue.js", "PostgreSQL", "MySQL", "Redis", "Jenkins", "CI/CD"};
        
        StringBuilder skills = new StringBuilder();
        for (String skill : commonSkills) {
            if (resumeContent.toLowerCase().contains(skill.toLowerCase())) {
                if (skills.length() > 0) skills.append(", ");
                skills.append(skill);
            }
        }
        return skills.toString();
    }
    
    public String extractExperience(String resumeContent) {
        String[] lines = resumeContent.split("\n");
        StringBuilder experience = new StringBuilder();
        boolean inExperienceSection = false;
        
        for (String line : lines) {
            if (line.toLowerCase().contains("experience") || line.toLowerCase().contains("work history")) {
                inExperienceSection = true;
                continue;
            }
            if (inExperienceSection && (line.toLowerCase().contains("education") || line.toLowerCase().contains("skills"))) {
                break;
            }
            if (inExperienceSection && !line.trim().isEmpty()) {
                experience.append(line.trim()).append("\n");
            }
        }
        return experience.toString();
    }
    
    public String extractEducation(String resumeContent) {
        String[] lines = resumeContent.split("\n");
        StringBuilder education = new StringBuilder();
        boolean inEducationSection = false;
        
        for (String line : lines) {
            if (line.toLowerCase().contains("education")) {
                inEducationSection = true;
                continue;
            }
            if (inEducationSection && (line.toLowerCase().contains("experience") || line.toLowerCase().contains("skills"))) {
                break;
            }
            if (inEducationSection && !line.trim().isEmpty()) {
                education.append(line.trim()).append("\n");
            }
        }
        return education.toString();
    }
}
